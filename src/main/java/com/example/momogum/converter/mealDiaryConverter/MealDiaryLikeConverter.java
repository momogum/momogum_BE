package com.example.momogum.converter.mealDiaryConverter;

import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryLikes;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.web.dto.mealDiary.MealDiaryLikeDTO;

public class MealDiaryLikeConverter {

    public static MealDiaryLikes toMealDiaryLikes(UserEntity userEntity, MealDiary mealDiary) {

        return MealDiaryLikes.builder()
                .userEntity(userEntity)
                .mealDiary(mealDiary)
                .build();
    }

    public static MealDiaryLikeDTO.MealDiaryLikeResponseDTO toMealDiaryLikeResponseDTO(UserEntity userEntity){
        return MealDiaryLikeDTO.MealDiaryLikeResponseDTO.builder()
            .userProfileImage(userEntity.getProfileImage() != null ? userEntity.getProfileImage().getImageLink() : "default-profile.jpg")
                .nickname(userEntity.getNickname())
                .name(userEntity.getName())
                .build();
    }
}
