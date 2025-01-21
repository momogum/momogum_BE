package com.example.momogum.web.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

// 카카오 API의 JSON 응답을 매핑하기 위한 DTO입니다.
@Getter
@Setter
@Schema(description = "카카오 API 응답 DTO")
public class KakaoResponseDTO {

    @Schema(description = "카카오 사용자 ID", example = "1234567890")
    private String id; // 카카오 사용자 ID

    @Schema(description = "카카오 계정 정보")
    private KakaoAccount kakao_account;

    @Getter
    @Setter
    @Schema(description = "카카오 계정 상세 정보")
    public static class KakaoAccount {

        @Schema(description = "카카오 프로필 정보")
        private KakaoProfile profile;

        @Getter
        @Setter
        @Schema(description = "카카오 프로필 상세 정보")
        public static class KakaoProfile {

            @Schema(description = "사용자 닉네임", example = "머머금")
            private String nickname; // 사용자 닉네임

            @Schema(description = "사용자 프로필 이미지 URL", example = "http://example.com/profile.png")
            private String profile_image_url; // 프로필 이미지 URL
        }
    }
}
