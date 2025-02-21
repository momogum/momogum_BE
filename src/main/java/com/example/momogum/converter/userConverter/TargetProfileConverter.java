package com.example.momogum.converter.userConverter;

import com.example.momogum.domain.UserEntity;
import com.example.momogum.web.dto.FollowDTO;
import com.example.momogum.web.dto.user.UserDTO;
import com.example.momogum.web.dto.viewMealDiary.ViewMealDiaryDTO.ViewMealDiaryResponse;
import java.util.List;

public class TargetProfileConverter {

  public static UserDTO.FullProfileDTO toFullProfileDTO(UserEntity user, List<ViewMealDiaryResponse> mealDiaries, boolean isFollow) {
    return UserDTO.FullProfileDTO.builder()
        .id(user.getId())
        .nickname(user.getNickname())
        .name(user.getName())
        .profileImageUrl(user.getProfileImage() != null ? user.getProfileImage().getImageLink() : "default-profile.jpg")
        .about(user.getAbout())
        .followerCount(user.getFollowerCount())
        .followingCount(user.getFollowingCount())
            .isFollowing(isFollow)
        .viewMealDiary(mealDiaries)
        .build();
  }

  /**
   * 상대방 프로필에서 팔로잉 정보를 변환
   */

  public static FollowDTO.FollowingResponseDTO toFollowingResponseDTO(UserEntity userEntity, Boolean isMutualFollow) {
    return FollowDTO.FollowingResponseDTO.builder()
        .userId(userEntity.getId())
        .name(userEntity.getName())
        .nickname(userEntity.getNickname())
        .profileImage(userEntity.getProfileImage() != null ? userEntity.getProfileImage().getImageLink() : null)
        .build();
  }

  /**
   * 상대방 프로필에서 팔로워 정보를 변환
   */

  public static FollowDTO.FollowerResponseDTO toFollowerResponseDTO(UserEntity userEntity, Boolean isMutualFollow) {
    return FollowDTO.FollowerResponseDTO.builder()
        .userId(userEntity.getId())
        .name(userEntity.getName())
        .nickname(userEntity.getNickname())
        .profileImage(userEntity.getProfileImage() != null ? userEntity.getProfileImage().getImageLink() : null)
        .build();
  }
}
