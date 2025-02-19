package com.example.momogum.web.dto.mealDiary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MealDiaryCommentDeleteDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MealDiaryCommentDeleteRequestDTO {

        Long mealDiaryCommentId;

    }
}
