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
}
