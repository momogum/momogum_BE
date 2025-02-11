package com.example.momogum.web.dto.mealDiary;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    }


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MealDiaryStoryReadAllResponseDTO {

        @Schema(description = "회원의 nickname입니다")
        String nickname;

        @Schema(description = "대표 이미지 링크입니다")
        String mealDiaryImageLinks;

    }
}
