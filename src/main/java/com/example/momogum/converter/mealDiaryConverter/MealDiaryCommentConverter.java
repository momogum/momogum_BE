package com.example.momogum.converter.mealDiaryConverter;

import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryComments;
import com.example.momogum.domain.UserEntity;

public class MealDiaryCommentConverter {

    public static MealDiaryComments toMealDiaryComments(String content,
                                                        MealDiary mealDiary,
                                                        UserEntity userEntity) {

        return MealDiaryComments.builder()
                .content(content)
                .mealDiary(mealDiary)
                .user(userEntity)
                .build();

    }
}
