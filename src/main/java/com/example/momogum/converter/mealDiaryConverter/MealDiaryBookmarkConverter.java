package com.example.momogum.converter.mealDiaryConverter;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.handler.ImageHandler;
import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryBookmark;
import com.example.momogum.domain.MealDiaryImage;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.web.dto.mealDiary.MealDairiesDTO;

public class MealDiaryBookmarkConverter {

    public static MealDiaryBookmark toMealDiaryBookmark(UserEntity userEntity, MealDiary mealDiary) {
        return MealDiaryBookmark.builder()
                .mealDiary(mealDiary)
                .userEntity(userEntity)
                .build();
    }

    public static MealDairiesDTO.GetAllMealDiaryResponseDTO toAllMealDiaryResponseDTO(MealDiary mealDiary){

        MealDiaryImage image = mealDiary.getMealDiaryImages().stream().findFirst()
                .orElseThrow(()-> new ImageHandler(ErrorStatus.IMAGE_NOT_FOUND));

        return MealDairiesDTO.GetAllMealDiaryResponseDTO.builder()
                .mealDiaryImageLink(image.getImageLink())
                .build();
    }
}
