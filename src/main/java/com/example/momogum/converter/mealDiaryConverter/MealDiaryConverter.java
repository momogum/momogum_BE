package com.example.momogum.converter.mealDiaryConverter;

import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryImage;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.web.dto.mealDiary.MealDairiesDTO;

import java.util.List;

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
                .userEntity(byId)
                // 밥일기 이미지 -> 추후 save로직에서 한 번에 이미지까지 받도록 수정 FIXME
                .mealDiaryImages(null)
                .build();

    }

    public static MealDairiesDTO.CreateStoryResponseDTO toCreateStoryResponseDTO(MealDiary newMealDiary){

        return MealDairiesDTO.CreateStoryResponseDTO.builder()
                .mealDiaryId(newMealDiary.getId())
                .build();
    }

    public static MealDairiesDTO.GetMealDiaryResponseDTO toGetMealDiaryResponseDTO(MealDiary mealDiary,
                                                                                   List<String> list,
                                                                                   List<String> mealDiaryImages,
                                                                                   boolean isLike,
                                                                                   boolean isBookmarked){

        return MealDairiesDTO.GetMealDiaryResponseDTO.builder()
                .userProfileImageLink(mealDiary.getUserEntity().getProfileImage())
                .nickname(mealDiary.getUserEntity().getNickname())
                .mealDiaryCreatedAt(mealDiary.getCreatedAt())
                .mealDiaryImageLinks(mealDiaryImages)
                .mealDiaryLikeCount(mealDiary.getLikesCount())
                .mealDiaryCommentCount(mealDiary.getCommentCount())
                .isMealDairyBookmark(isBookmarked)
                .location(mealDiary.getLocation())
                .keywords(list)
                .review(mealDiary.getDescription())
                .isRevisit(mealDiary.getIsRevisit())
                .isLike(isLike)
                .build();
    }

    public static MealDairiesDTO.GetAllMealDiaryResponseDTO toGetAllMealDiaryResponseDTO(MealDiaryImage mealDiary){
        return MealDairiesDTO.GetAllMealDiaryResponseDTO.builder()
                .mealDiaryImageLink(mealDiary.getImageLink())
                .build();
    }
}
