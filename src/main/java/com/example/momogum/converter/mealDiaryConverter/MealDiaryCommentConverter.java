package com.example.momogum.converter.mealDiaryConverter;

import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryComments;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.web.dto.mealDiary.MealDiaryCommentReadDTO;

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

    public static MealDiaryCommentReadDTO.MealDiaryReadResponseDTO toMealDiaryCommentReadDTO(MealDiaryComments comment){

        return MealDiaryCommentReadDTO.MealDiaryReadResponseDTO.builder()
                .userProfileImagePath(comment.getUser().getProfileImage())
                .nickname(comment.getUser().getNickname())
                .content(comment.getContent())
                .build();
    }
}
