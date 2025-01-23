package com.example.momogum.converter;

import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryBookmark;
import com.example.momogum.domain.UserEntity;

public class MealDiaryBookmarkConverter {

    public static MealDiaryBookmark toMealDiaryBookmark(UserEntity userEntity, MealDiary mealDiary) {
        return MealDiaryBookmark.builder()
                .mealDiary(mealDiary)
                .userEntity(userEntity)
                .build();
    }
}
