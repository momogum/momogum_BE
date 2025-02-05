package com.example.momogum.web.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
        // 밥일기 피드입니다
        // private List<ViewMealDiaryDTO> viewMealDiary;
    }

    /**
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ViewMealDiaryDTO {

    @Schema(description = "밥일기 Id")
    private Long mealDiaryId;

    @Schema(description = "음식 사진 URL 입니다.")
    private List<String> foodImageURLs;


    @Schema(description = "음식 카테고리 (한식(KOREA),중식(CHINA),일식(JAPAN),아시안(ASIAN),패스트푸드(FASTFOOD),카페(CAFE)")
    private FoodCategory foodCategory;

    @Schema(description = "음식 이름")
    private List<String> keyWord;

    @Schema(description = "재방문 의사 표시 (REVISIT 이 재방문)")
    private IsRevisit isRevisit;

    }
    */

}
