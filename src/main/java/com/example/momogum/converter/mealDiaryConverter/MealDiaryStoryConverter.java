package com.example.momogum.converter.mealDiaryConverter;

import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryImage;
import com.example.momogum.domain.MealDiaryStory;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.web.dto.mealDiary.MealDiaryStoryReadDTO;

import java.util.List;

public class MealDiaryStoryConverter {

    public static MealDiaryStoryReadDTO.MealDiaryStoryReadResponseDTO toMealDiaryStoryReadDTO(MealDiaryStory mealDiaryStory,
                                                                                              List<String> imageLinks) {

        return MealDiaryStoryReadDTO.MealDiaryStoryReadResponseDTO.builder()
                .name(mealDiaryStory.getName())
                .mealDiaryImageLinks(imageLinks)
                .description(mealDiaryStory.getMealDiary().getDescription())
                .location(mealDiaryStory.getMealDiary().getDescription())
                .build();
    }

    public static List<MealDiaryStoryReadDTO.MealDiaryStoryReadAllResponseDTO> toMealDiaryStoryReadAllDTO(List<MealDiaryStory> mealDiaryStoryList) {
        return mealDiaryStoryList.stream().map(mealDiaryStory -> {

            List<MealDiaryImage> mealDiaryImages = mealDiaryStory.getMealDiary().getMealDiaryImages();
            String name = mealDiaryStory.getName();
            String imageLink = (mealDiaryImages != null && !mealDiaryImages.isEmpty()) ? mealDiaryImages.get(0).getImageLink() : null;

            return MealDiaryStoryReadDTO.MealDiaryStoryReadAllResponseDTO.builder()
                    .mealDiaryImageLinks(imageLink)
                    .nickname(name)
                    .build();
        }).toList();
    }

    public static MealDiaryStoryReadDTO.MyMealDiaryStoryReadResponseDTO toMyMealDiaryStoryReadDTO(UserEntity findUser, String imageLink, boolean isViewed) {

        return MealDiaryStoryReadDTO.MyMealDiaryStoryReadResponseDTO.builder()
                .nickname(findUser.getNickname())
                .mealDiaryImageLinks(imageLink)
                .isViewed(isViewed)
                .build();
    }

}
