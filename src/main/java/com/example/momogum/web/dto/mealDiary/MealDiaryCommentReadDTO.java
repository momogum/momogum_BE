package com.example.momogum.web.dto.mealDiary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MealDiaryCommentReadDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MealDiaryReadResponseDTO{

        // 기획안이 나오는대로 FIXME

        String userProfileImagePath;
        String nickname;

        String content;

    }
}
