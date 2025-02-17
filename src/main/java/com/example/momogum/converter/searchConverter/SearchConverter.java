package com.example.momogum.converter.searchConverter;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.handler.ImageHandler;
import com.example.momogum.domain.*;
import com.example.momogum.web.dto.search.SearchDTO;

import java.util.List;
import java.util.Optional;

public class SearchConverter {

    public static SearchDTO.PostSearchResponseDTO toPostSearchResponseDTO(MealDiary mealDiary, String searchQuery, List<String> splitKeywords) {

        String matchedKeyword = mealDiary.getMealDiaryKeywords().stream()
                .map(MealDiaryKeyword::getKeyword)
                .map(Keyword::getKeyword)
                .filter(keyword -> keyword.equalsIgnoreCase(searchQuery) // 전체 검색어 일치
                        || keyword.equalsIgnoreCase(searchQuery.replaceAll("\\s+", "")) // 공백 제거 후 일치
                        || splitKeywords.contains(keyword)) // 분리된 단어 중 일치
                .findFirst()
                .orElse("n/a");

        return SearchDTO.PostSearchResponseDTO.builder()
                .mealDiaryId(mealDiary.getId())
                .foodImageURL(mealDiary.getMealDiaryImages().stream().findFirst()
                        .map(MealDiaryImage::getImageLink).orElse(null))
                .userImageURL(mealDiary.getUserEntity().getProfileImage().getImageLink())
                .foodName(matchedKeyword)
                .isRevisit(mealDiary.getIsRevisit())
                .build();
    }

    public static SearchDTO.AccountSearchResponseDTO toAccountSearchResponseDTO(UserEntity user, List<String> followNames, Integer count) {

        return SearchDTO.AccountSearchResponseDTO.builder()
                .userId(user.getId())
                .userName(user.getName())
                .userNickName(user.getNickname())
                .userImageURL(Optional.ofNullable(user.getProfileImage())
                        .map(ProfileImage::getImageLink)
                        .orElseThrow(() -> new ImageHandler(ErrorStatus.PROFILE_IMAGE_NOT_FOUND)))
                .searchFollowName(followNames)
                .searchFollowCount(count)
                .build();
    }

    public static SearchDTO.FollowerSearchResponseDTO toFollowerSearchResponseDTO(UserEntity user) {

        return SearchDTO.FollowerSearchResponseDTO.builder()
            .userId(user.getId())
            .userName(user.getName())
            .userNickName(user.getNickname())
            .userImageURL(user.getProfileImage().getImageLink())
            .build();
    }

    public static SearchDTO.FollowingSearchResponseDTO toFollowingSearchResponseDTO(UserEntity user) {

        return SearchDTO.FollowingSearchResponseDTO.builder()
            .userId(user.getId())
            .userName(user.getName())
            .userNickName(user.getNickname())
            .userImageURL(user.getProfileImage().getImageLink())
            .build();
    }

}
