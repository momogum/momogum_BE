package com.example.momogum.web.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserResponseDTO {

        @Schema(description = "유저 ID 입니다.")
        Long id;

        @Schema(description = "유저 이름입니다.")
        String name;

        @Schema(description = "유저 닉네임입니다.")
        String nickname;

        @Schema(description = "유저 프로필 이미지 URL입니다.")
        String profileImage;

        @Schema(description = "신규 사용자 여부입니다.")
        boolean isNewUser;
    }

}
