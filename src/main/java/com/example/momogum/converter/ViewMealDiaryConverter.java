package com.example.momogum.converter;


import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryBookmark;
import com.example.momogum.domain.MealDiaryImage;
import com.example.momogum.web.dto.viewMealDiary.ViewMealDiaryDTO.ViewMealDiaryResponse;
import com.example.momogum.web.dto.viewMealDiary.ViewMealDiaryDTO.MainViewMealDiaryResponse;

import java.util.stream.Collectors;

public class ViewMealDiaryConverter {

    public static ViewMealDiaryResponse toViewMealDiaryResponse(MealDiary mealDiary) {
        return ViewMealDiaryResponse.builder()
                .mealDiaryId(mealDiary.getId())
//      .foodImageURLs(mealDiary.getMealDiaryImages().stream()
//        .map(MealDiaryImage::getImageLink)
//        .toList()) 밥일기 이미지 구현되면 수정하겠습니다.
                .userImageURL(mealDiary.getUserEntity().getProfileImage() != null
                        ? mealDiary.getUserEntity().getProfileImage().getImageLink()
                        : "https://momogum-bucket.s3.ap-northeast-2.amazonaws.com/basic_profile/DEFAULT_IMAGE.webp")
                .foodCategory(mealDiary.getFoodCategory())
                .keyWord(mealDiary.getMealDiaryKeywords().stream()
                        .map(mealDiaryKeyword -> mealDiaryKeyword.getKeyword().getKeyword())
                        .collect(Collectors.toList()))
                .isRevisit(mealDiary.getIsRevisit())
                .build();
    }

    public static ViewMealDiaryResponse toViewMealDiaryResponse(MealDiaryBookmark bookmark) {
        return ViewMealDiaryResponse.builder()
                .mealDiaryId(bookmark.getMealDiary().getId())
//      .foodImageURLs(mealDiary.getMealDiaryImages().stream()
//        .map(MealDiaryImage::getImageLink)
//        .toList())
                .userImageURL(bookmark.getMealDiary().getUserEntity().getProfileImage() != null
                        ? bookmark.getMealDiary().getUserEntity().getProfileImage().getImageLink()
                        : "https://momogum-bucket.s3.ap-northeast-2.amazonaws.com/basic_profile/DEFAULT_IMAGE.webp")
                .foodCategory(bookmark.getMealDiary().getFoodCategory())
                .keyWord(bookmark.getMealDiary().getMealDiaryKeywords().stream()
                        .map(mealDiaryKeyword -> mealDiaryKeyword.getKeyword().getKeyword())
                        .collect(Collectors.toList()))
                .isRevisit(bookmark.getMealDiary().getIsRevisit())
                .build();
    }

    public static MainViewMealDiaryResponse toMainViewMealDiaryResponse(MealDiary mealDiary) {
        return MainViewMealDiaryResponse.builder()
                .mealDiaryId(mealDiary.getId())
                .foodImageURLs(mealDiary.getMealDiaryImages().stream()
                        .map(MealDiaryImage::getImageLink)
                        .toList())
                .userImageURL(mealDiary.getUserEntity().getProfileImage() != null
                        ? mealDiary.getUserEntity().getProfileImage().getImageLink()
                        : "default-profile.jpg")
                .foodCategory(mealDiary.getFoodCategory())
                .keyWord(mealDiary.getMealDiaryKeywords().stream()
                        .map(mealDiaryKeyword -> mealDiaryKeyword.getKeyword().getKeyword())
                        .findFirst()
                        .orElse("default"))
                .isRevisit(mealDiary.getIsRevisit())
                .build();
    }
}