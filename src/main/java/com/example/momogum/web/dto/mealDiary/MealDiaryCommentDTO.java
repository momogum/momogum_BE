package com.example.momogum.web.dto.mealDiary;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MealDiaryCommentDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MealDiaryCommentRequestDTO{

        Long userId;

        Long mealDiaryId;

        String comment;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MealDiaryCommentResponseDTO{

        @Schema(name = "밥일기 댓글 식별자")
        Long mealDiaryCommentId;

    }
}
