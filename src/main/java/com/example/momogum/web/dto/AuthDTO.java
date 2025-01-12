package com.example.momogum.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthRequestDTO {

        @Schema(description = "소셜 로그인 제공자 (예: kakao, apple)", example = "kakao")
        private String provider;

        @Schema(description = "사용자의 이름", example = "John Doe")
        private String name;

        @Schema(description = "사용자의 이메일 주소", example = "user@example.com")
        private String email;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthResponseDTO {

        @Schema(description = "사용자의 이메일 주소", example = "user@example.com")
        private String email;

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
}
