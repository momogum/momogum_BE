package com.example.momogum.web.dto.mealDiary;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MealDiaryCommentUpdateDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MealDiaryCommentUpdateRequestDTO {

        Long userId;

        Long mealDiaryCommentId;

        String comment;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MealDiaryCommentUpdateResponseDTO {

        @Schema(name = "밥일기 댓글 식별자")
        Long mealDiaryCommentId;

    }
}
