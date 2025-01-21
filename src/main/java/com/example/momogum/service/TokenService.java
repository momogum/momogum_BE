package com.example.momogum.service;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.common.enums.LoginType;
import com.example.momogum.web.dto.user.KakaoResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.Map;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;


@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final UserEntityRepository userEntityRepository;
    private final RedisTemplate<String, String> redisTemplate;

    // Access Token 생성
    public String createAccessToken(String userId) {
        return "access-token-" + userId;
    }

    // Refresh Token 생성
    public String createRefreshToken(String userId) {
        return "refresh-token-" + userId;
    }

    // 기존 유저 로그인 처리
    @Transactional
    public UserEntity processExistingUserLogin(String accessToken) {
        KakaoResponseDTO kakaoResponseDTO = fetchKakaoUserInfo(accessToken);

        String providerId = kakaoResponseDTO.getId();
        String profileImage = kakaoResponseDTO.getKakao_account().getProfile().getProfile_image_url();

        return userEntityRepository.findByProviderAndProviderId(LoginType.KAKAO, providerId)
                .map(existingUser -> {
                    existingUser.setProfileImage(profileImage); // 프로필 이미지만 업데이트
                    return userEntityRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("기존 유저를 찾을 수 없습니다."));
    }

    // 신규 유저 로그인 처리
    @Transactional
    public UserEntity processNewUserLogin(String accessToken, String nameInput, String nicknameInput) {
        KakaoResponseDTO kakaoResponseDTO = fetchKakaoUserInfo(accessToken);

        String providerId = kakaoResponseDTO.getId();
        String profileImage = kakaoResponseDTO.getKakao_account().getProfile().getProfile_image_url();

        UserEntity newUser = UserEntity.builder()
                .provider(LoginType.KAKAO)
                .providerId(providerId)
                .name(nameInput) // 입력된 이름
                .nickname(nicknameInput) // 입력된 닉네임
                .profileImage(profileImage)
                .build();
        return userEntityRepository.save(newUser);
    }

    // 유저 존재 여부 확인
    public boolean isExistingUser(String accessToken) {
        KakaoResponseDTO kakaoResponseDTO = fetchKakaoUserInfo(accessToken);
        String providerId = kakaoResponseDTO.getId();
        return userEntityRepository.findByProviderAndProviderId(LoginType.KAKAO, providerId).isPresent();
    }

    // 카카오 API 호출 로직 분리
    public KakaoResponseDTO fetchKakaoUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<KakaoResponseDTO> response = restTemplate.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.GET,
                    entity,
                    KakaoResponseDTO.class
            );

            log.info("카카오 API 호출 성공 - 상태 코드: {}", response.getStatusCode());
            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.error("카카오 API 호출 실패 - 상태 코드: {}", e.getStatusCode());
            log.error("카카오 API 오류 응답 본문: {}", e.getResponseBodyAsString());
            throw new RuntimeException("카카오 API 호출 실패: " + e.getMessage());
        }
    }


    public String requestAccessTokenFromKakao(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 카카오 OAuth 인증 과정에서 액세스 토큰 요청을 요청하기 위해 필요한 파라미터 정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code"); // 인증 방식
        params.add("client_id", "832eb815f100be4eb52994b0be137716"); // 카카오 앱 REST API 키
        params.add("redirect_uri", "http://localhost:8080/auth/callback/kakao"); // 리디렉션 URI
        params.add("code", code);
        log.info("카카오 액세스 토큰 요청 파라미터: {}", params);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        // 카카오 액세스 토큰 요청
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://kauth.kakao.com/oauth/token",
                request,
                Map.class
        );

        // 응답에서 액세스 토큰 추출
        Map<String, Object> body = response.getBody();
        if (body == null || !body.containsKey("access_token")) {
            throw new RuntimeException("카카오 액세스 토큰 요청 실패");
        }

        return (String) body.get("access_token");
    }

    // 토큰 저장
    public void saveTokens(String accessToken, String refreshToken, String userId) {
        redisTemplate.opsForValue().set(accessToken, userId, 30, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(refreshToken, userId, 7, TimeUnit.DAYS);
    }

    public Optional<UserEntity> findUserById(Long userId) {
        return userEntityRepository.findById(userId);
    }
}