package com.example.momogum.service;

import com.example.momogum.apiPayLoad.exception.DuplicateUserException;
import com.example.momogum.domain.ProfileImage;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.common.enums.LoginType;
import com.example.momogum.domain.utils.JwtUtil;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.user.KakaoResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final UserEntityRepository userEntityRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;

    // JWT Access Token 생성
    public String createAccessToken(String userId) {
        return jwtUtil.generateAccessToken(userId);
    }

    // JWT Refresh Token 생성
    public String createRefreshToken(String userId) {
        return jwtUtil.generateRefreshToken(userId);
    }

    // 기존 유저 로그인 처리
    @Transactional
    public UserEntity processExistingUserLoginByProviderId(String providerId) {
        return userEntityRepository.findByProviderAndProviderId(LoginType.KAKAO, providerId)
                .orElseThrow(() -> new RuntimeException("기존 유저를 찾을 수 없습니다."));
    }


    // 신규 유저 로그인 처리
    @Transactional
    public UserEntity processNewUserLogin(String providerId, String nameInput, String nicknameInput, String profileImageUrl) {
        // providerId 중복 여부 확인
        if (userEntityRepository.findByProviderAndProviderId(LoginType.KAKAO, providerId).isPresent()) {
            throw new DuplicateUserException("이미 등록된 providerId입니다: " + providerId);
        }

        // 신규 사용자 생성
        UserEntity newUser = UserEntity.builder()
                .provider(LoginType.KAKAO)
                .providerId(providerId)
                .name(nameInput)
                .nickname(nicknameInput)
                .build();

        // 프로필 이미지가 존재하면 ProfileImage 엔티티 생성 및 연관 관계 설정
        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
            ProfileImage profileImage = ProfileImage.builder()
                    .imageLink(profileImageUrl)  // S3 이미지 링크
                    .fileName("example-file-name")  // 실제 파일 이름
                    .imageName("example-original-name")  // 원본 파일 이름
                    .build();

            // 양방향 연관 관계 설정
            profileImage.setUser(newUser);
            newUser.setProfileImage(profileImage);
        }

        return userEntityRepository.save(newUser);
    }





    // 유저 존재 여부 확인
    public boolean isExistingUserByProviderId(String providerId) {
        return userEntityRepository.findByProviderAndProviderId(LoginType.KAKAO, providerId).isPresent();
    }


    // 카카오 API 호출 로직
    public KakaoResponseDTO fetchKakaoUserInfo(String kakaoAccessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + kakaoAccessToken);

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

    // 카카오 인증 서버에서 Access Token 요청
    public String requestAccessTokenFromKakao(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "832eb815f100be4eb52994b0be137716");
        params.add("redirect_uri", "http://localhost:8080/auth/callback/kakao");
        params.add("code", code);
        log.info("카카오 액세스 토큰 요청 파라미터: {}", params);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://kauth.kakao.com/oauth/token",
                request,
                Map.class
        );

        Map<String, Object> body = response.getBody();
        if (body == null || !body.containsKey("access_token")) {
            throw new RuntimeException("카카오 액세스 토큰 요청 실패");
        }

        return (String) body.get("access_token");
    }

    // Redis에 JWT 저장
    public void saveTokens(String accessToken, String refreshToken, String userId) {
        redisTemplate.opsForValue().set(accessToken, userId, 30, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(refreshToken, userId, 7, TimeUnit.DAYS);
    }

    // 유저 ID로 유저 조회
    public Optional<UserEntity> findUserById(Long userId) {
        return userEntityRepository.findById(userId);
    }
}
