package com.example.momogum.converter.followConverter;

import com.example.momogum.domain.UserEntity;
import com.example.momogum.web.dto.FollowDTO;

public class TargetFollowConverter {

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