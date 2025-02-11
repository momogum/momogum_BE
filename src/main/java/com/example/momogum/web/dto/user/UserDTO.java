package com.example.momogum.web.dto.user;

import com.example.momogum.web.dto.viewMealDiary.ViewMealDiaryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

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

        @Schema(description = "유저 한줄 소개입니다.")
        String about;

        @Schema(description = "유저 프로필 이미지 URL입니다.")
        String profileImage;

        @Schema(description = "신규 사용자 여부입니다.")
        boolean isNewUser;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserEditDTO {

        @Schema(description = "유저 네임", example = "kyum", required = true)
        @NotBlank(message = "닉네임은 필수입니다")
        @Length(min = 2, max = 20, message = "닉네임은 2글자 이상 20글자 이하로 입력해주세요.")
        private String nickname;

        @Schema(description = "유저 실명", example = "규민", required = true)
        @NotBlank(message = "이름을 입력해주세요")
        @Length(min = 2, max = 20, message = "이름은 2글자 이상 20글자 이하로 입력해주세요.")
        private String name;

        // 한 줄 소개 입력은 사용자 선택사항
        @Schema(description = "한줄 소개", example = "덕규입니다", required = false)
        @Length(min = 2, max = 20, message = "한줄 소개는 2글자 이상 20글자 이하로 입력해주세요.")
        private String about;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
        public static class FullProfileDTO {
        private Long id;
        @Schema(description = "유저 네임")
        private String nickname;
        @Schema(description = "유저 실명")
        private String name;
        @Schema(description = "프로필 이미지")
        private String profileImageUrl;
        @Schema(description = "한줄 소개")
        private String about;
        @Schema(description = "팔로워 수")
        private int followerCount;
        @Schema(description = "팔로잉 수")
        private int followingCount;
        @Schema(description = "팔로잉 여부")
        private Boolean isFollowing;
        @Schema(description = "밥일기 피드")
        private List<ViewMealDiaryDTO.ViewMealDiaryResponse> viewMealDiary;
    }

}
