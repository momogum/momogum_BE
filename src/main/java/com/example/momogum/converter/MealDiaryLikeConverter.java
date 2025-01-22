package com.example.momogum.converter;

import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryLikes;
import com.example.momogum.domain.UserEntity;

public class MealDiaryLikeConverter {

    public static MealDiaryLikes toMealDiaryLikes(UserEntity userEntity, MealDiary mealDiary) {

        return MealDiaryLikes.builder()
                .userEntity(userEntity)
                .mealDiary(mealDiary)
                .build();
    }
}
