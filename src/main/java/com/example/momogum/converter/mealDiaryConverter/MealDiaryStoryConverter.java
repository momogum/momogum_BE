package com.example.momogum.converter.mealDiaryConverter;

import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryImage;
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
        return mealDiaryStoryList.stream().map(mealDiaryStory -> {
            List<MealDiaryImage> mealDiaryImages = mealDiaryStory.getMealDiary().getMealDiaryImages();
            String imageLink = (mealDiaryImages != null && !mealDiaryImages.isEmpty()) ? mealDiaryImages.get(0).getImageLink() : null;
            return MealDiaryStoryReadDTO.MealDiaryStoryReadAllResponseDTO.builder()
                    .mealDiaryImageLinks(imageLink)
                    .build();
        }).toList();
    }

}
