package com.example.momogum.web.dto.mealDiary;

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

        String name;

        List<String> mealDiaryImageLinks;
    }
}
