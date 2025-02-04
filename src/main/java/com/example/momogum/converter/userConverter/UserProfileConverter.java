package com.example.momogum.converter.userConverter;

import com.example.momogum.domain.UserEntity;
import com.example.momogum.web.dto.user.UserDTO;

public class UserProfileConverter {

  public static UserDTO.FullProfileDTO toFullProfileDTO(UserEntity user) {
    return UserDTO.FullProfileDTO.builder()
        .nickname(user.getNickname())
        .name(user.getName())
        .profileImageUrl(user.getProfileImage() != null ? user.getProfileImage().getImageLink() : null)
        .about(user.getAbout())
        .followerCount(user.getFollowerCount())
        .followingCount(user.getFollowingCount())
        //.<List>viewMealDiray
        .build();
  }
}
