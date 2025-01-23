package com.example.momogum.web.dto.mealDiary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MealDiaryLikeDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MealDiaryLikeResponseDTO {

        String userProfileImage;

        String nickname;

        String name;

        // 회원의 팔로우 상태도 반환해야함 FIXME
    }
}
