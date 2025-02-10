package com.example.momogum.converter.searchConverter;

import com.example.momogum.domain.*;
import com.example.momogum.domain.common.enums.IsRevisit;
import com.example.momogum.web.dto.search.SearchDTO;

import java.util.List;

public class SearchConverter {

    public static SearchDTO.PostSearchResponseDTO toPostSearchResponseDTO(MealDiary mealDiary) {

        return SearchDTO.PostSearchResponseDTO.builder()
                .mealDiaryId(mealDiary.getId())
                .foodImageURL(mealDiary.getMealDiaryImages().stream().findFirst().map(MealDiaryImage::getImageLink).orElse(null))
                .userImageURL(mealDiary.getUserEntity().getProfileImage().getImageLink())
                .foodName(mealDiary.getMealDiaryKeywords().stream().findFirst().map(MealDiaryKeyword::getKeyword).map(Keyword::getKeyword).orElse("n/a"))
                .isRevisit(mealDiary.getIsRevisit())
                .build();
    }

    public static SearchDTO.AccountSearchResponseDTO toAccountSearchResponseDTO(UserEntity user, List<String> followNames, Integer count) {

        return SearchDTO.AccountSearchResponseDTO.builder()
                .userId(user.getId())
                .userName(user.getName())
                .userNickName(user.getNickname())
                .userImageURL(user.getProfileImage().getImageLink())
                .searchFollowName(followNames)
                .searchFollowCount(count)
                .build();
    }

}
