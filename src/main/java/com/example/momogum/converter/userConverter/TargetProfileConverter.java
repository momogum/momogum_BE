package com.example.momogum.converter.userConverter;

import com.example.momogum.domain.UserEntity;
import com.example.momogum.web.dto.user.UserDTO;
import com.example.momogum.web.dto.viewMealDiary.ViewMealDiaryDTO;
import com.example.momogum.web.dto.viewMealDiary.ViewMealDiaryDTO.ViewMealDiaryResponse;
import java.util.List;

public class TargetProfileConverter {

  /**
   * 특정 유저의 프로필 정보를 FullProfileDTO로 변환
   */
  public static UserDTO.FullProfileDTO toFullProfileDTO(UserEntity user, List<ViewMealDiaryResponse> mealDiaries, boolean isFollowing) {
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
        .isFollowing(isFollowing)
        .build();
  }

}
