package com.example.momogum.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 클라이언트의 요청과 응답을 처리하는 DTO
public class AuthDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthRequestDTO {

        @Schema(description = "소셜 로그인 제공자 (예: kakao, apple)", example = "kakao")
        private String provider;

        @Schema(description = "소셜 로그인 제공자로부터 받은 Access Token", example = "your-access-token-from-provider")
        private String accessToken;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class AuthResponseDTO {

        @Schema(description = "사용자의 이름", example = "John Doe")
        private String name;

        @Schema(description = "소셜 로그인 제공자", example = "kakao")
        private String provider;
    }

    @Getter
    @Builder
    public static class TokenResponseDTO {

        @Schema(description = "JWT Access Token. API 요청 시 인증에 사용합니다.", example = "eyJhbGciOiJIUzUxMiJ9...")
        private String accessToken;

        @Schema(description = "JWT Refresh Token. Access Token이 만료되었을 때 새로운 Access Token을 발급받기 위해 사용합니다.", example = "eyJhbGciOiJIUzUxMiJ9...")
        private String refreshToken;
    }
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUpRequestDTO {

        @Schema(description = "카카오 액세스 토큰", example = "your-access-token")
        private String accessToken;

        @Schema(description = "사용자가 입력한 이름", example = "홍길동")
        private String name;

        @Schema(description = "사용자가 입력한 닉네임", example = "길동이")
        private String nickname;


        // FCM 토큰 받기
        private String fcmToken;
    }

}
