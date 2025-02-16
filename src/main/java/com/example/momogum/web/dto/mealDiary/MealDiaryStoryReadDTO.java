package com.example.momogum.web.dto.mealDiary;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class MealDiaryStoryReadDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MealDiaryStoryReadResponseDTO {

        @Schema(description = "회원의 name입니다")
        String name;

        @Schema(description = "이미지 링크입니다")
        List<String> mealDiaryImageLinks;

        @Schema(description = "스토리를 작성한 회원의 프로필 이미지입니다")
        String profileImageLink;

        @Schema(description = "식당이름 입니다")
        String location;

        @Schema(description = "식당후기 입니다")
        String description;

        @Schema(description = "스토리 생성일자")
        LocalDateTime createdAt;
    }


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MealDiaryStoryReadAllResponseDTO {

        @Schema(description = "스토리 식별자입니다")
        Long mealDiaryStoryId;

        @Schema(description = "스토리를 작성한 회원의 nickname입니다")
        String nickname;

        @Schema(description = "스토리를 작성한 회원의 프로필 이미지입니다")
        String profileImageLink;

        @Schema(description = "스토리 메인 이미지입니다")
        String mealDiaryImageLinks;

        @Schema(description = "스토리 조회 여부 입니다")
        boolean isViewed;

        @Schema(description = "스토리 조회를 최신차 순으로 조회하기 위한 필드 입니다")
        LocalDateTime createdAt;

    }


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyMealDiaryStoryReadResponseDTO{

        @Schema(description = "스토리 식별자입니다")
        Long mealDiaryStoryId;

        @Schema(description = "회원의 nickname입니다")
        String nickname;

        @Schema(description = "스토리를 작성한 회원의 프로필 이미지입니다")
        String profileImageLink;

        @Schema(description = "스토리 메인 이미지입니다")
        String mealDiaryImageLinks;

        @Schema(description = "스토리 조회 여부 입니다")
        boolean isViewed;

    }
}
