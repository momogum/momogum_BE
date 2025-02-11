package com.example.momogum.converter.userConverter;

import com.example.momogum.domain.UserEntity;
import com.example.momogum.web.dto.user.UserDTO;
import com.example.momogum.web.dto.viewMealDiary.ViewMealDiaryDTO;
import com.example.momogum.web.dto.viewMealDiary.ViewMealDiaryDTO.ViewMealDiaryResponse;
import java.util.List;

public class UserProfileConverter {

  public static UserDTO.FullProfileDTO toFullProfileDTO(UserEntity user, List<ViewMealDiaryResponse> mealDiaries) {
    return UserDTO.FullProfileDTO.builder()
        .id(user.getId())
        .nickname(user.getNickname())
        .name(user.getName())
        .profileImageUrl(user.getProfileImage() != null
            ? user.getProfileImage().getImageLink()
            : "default-profile.jpg")
        .about(user.getAbout())
        .followerCount(user.getFollowerCount())
        .followingCount(user.getFollowingCount())
        .viewMealDiary(mealDiaries)
        .build();
  }
}