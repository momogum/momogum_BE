package com.example.momogum.converter.mealDiaryConverter;

import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryStory;
import com.example.momogum.web.dto.mealDiary.MealDiaryStoryReadDTO;

import java.util.List;

public class MealDiaryStoryConverter {

    public static MealDiaryStoryReadDTO.MealDiaryStoryReadResponseDTO toMealDiaryStoryReadDTO(MealDiaryStory mealDiaryStory,
                                                                                              List<String> imageLinks) {

        return MealDiaryStoryReadDTO.MealDiaryStoryReadResponseDTO.builder()
                .name(mealDiaryStory.getName())
                .mealDiaryImageLinks(imageLinks)
                .build();
    }

    public static List<MealDiaryStoryReadDTO.MealDiaryStoryReadAllResponseDTO> toMealDiaryStoryReadAllDTO(List<MealDiaryStory> mealDiaryStoryList) {
        return mealDiaryStoryList.stream().map(mealDiaryStory ->
                MealDiaryStoryReadDTO.MealDiaryStoryReadAllResponseDTO.builder()
                        .mealDiaryImageLinks(mealDiaryStory.getMealDiary().getMealDiaryImages().get(0).getImageLink())
                        .build()
        ).toList();
    }
}
