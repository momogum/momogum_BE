package com.example.momogum.converter;

import com.example.momogum.domain.Keyword;
import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryKeyword;

public class MealDiaryKeywordConverter {

    public static MealDiaryKeyword toMealDiaryKeyword(MealDiary newMealDiary, Keyword keyword) {
        return MealDiaryKeyword.builder()
                .mealDiary(newMealDiary)
                .keyword(keyword)
                .build();
    }
}
