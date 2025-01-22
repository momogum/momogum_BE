package com.example.momogum.converter;

import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.web.dto.MealDairiesDTO;

public class MealDiaryConverter {

    public static MealDiary toMealDiary(MealDairiesDTO.CreateStoryRequestDTO request, UserEntity byId){

        return MealDiary.builder()
                .foodCategory(request.getFoodCategory())
                .location(request.getLocation())
                .description(request.getDescription())
                .isRevisit(request.getRevisit())
                .isReport(false)
                .likesCount(0)
                .commentCount(0)
                .user(byId)
                // 밥일기 이미지 -> 추후 save로직에서 한 번에 이미지까지 받도록 수정 FIXME
                .mealDiaryImages(null)
                .build();

    }

    public static MealDairiesDTO.CreateStoryResponseDTO toCreateStoryResponseDTO(MealDiary newMealDiary){

        return MealDairiesDTO.CreateStoryResponseDTO.builder()
                .mealDiaryId(newMealDiary.getId())
                .build();
    }
}
