package com.example.momogum.service;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.common.enums.LoginType;
import com.example.momogum.web.dto.user.KakaoResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.Map;

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

    // 로그인 처리 과정
    @Transactional
    public UserEntity processUserLogin(String accessToken) {
        // RestTemplate 설정
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        log.info("사용된 액세스 토큰: {}", accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // 카카오 API 호출
            ResponseEntity<KakaoResponseDTO> response = restTemplate.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.GET,
                    entity,
                    KakaoResponseDTO.class
            );

            // 상태 코드와 응답 로그 출력
            log.info("카카오 API 호출 성공 - 상태 코드: {}", response.getStatusCode());
            log.info("카카오 API 응답 본문: {}", response.getBody());

            // 응답 데이터에서 필요한 정보 추출
            KakaoResponseDTO kakaoResponseDTO = response.getBody();
            if (kakaoResponseDTO == null) {
                throw new RuntimeException("카카오 API 호출 실패: 응답이 비어 있습니다.");
            }

            String providerId = kakaoResponseDTO.getId();
            String nickname = kakaoResponseDTO.getKakao_account().getProfile().getNickname();
            String profileImage = kakaoResponseDTO.getKakao_account().getProfile().getProfile_image_url();

            // 사용자 DB 저장 또는 업데이트
            return userEntityRepository.findByProviderAndProviderId(LoginType.KAKAO, providerId)
                    .map(existingUser -> {
                        existingUser.setNickname(nickname);
                        existingUser.setProfileImage(profileImage);
                        return userEntityRepository.save(existingUser);
                    })
                    .orElseGet(() -> {
                        UserEntity newUser = UserEntity.builder()
                                .provider(LoginType.KAKAO)
                                .providerId(providerId)
                                .nickname(nickname)
                                .profileImage(profileImage)
                                .build();
                        return userEntityRepository.save(newUser);
                    });
        } catch (HttpClientErrorException e) {
            // HTTP 상태 코드와 오류 본문 디버깅
            log.error("카카오 API 호출 실패 - 상태 코드: {}", e.getStatusCode());
            log.error("카카오 API 오류 응답 본문: {}", e.getResponseBodyAsString());
            throw new RuntimeException("카카오 API 호출 실패: " + e.getMessage());
        } catch (Exception e) {
            // 기타 예외 디버깅
            log.error("카카오 API 호출 중 예외 발생", e);
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
}