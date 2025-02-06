package com.example.momogum.converter.followConverter;

import com.example.momogum.domain.UserEntity;
import com.example.momogum.web.dto.FollowDTO;

public class FollowConverter {

  /**
   * 팔로우한 유저 정보를 FollowResponseDTO로 변환
   */
  public static FollowDTO.FollowingResponseDTO toFollowingResponseDTO(UserEntity userEntity, Boolean isMutualFollow) {
    return FollowDTO.FollowingResponseDTO.builder()
        .name(userEntity.getName())
        .nickname(userEntity.getNickname())
        .profileImage(userEntity.getProfileImage() != null ? userEntity.getProfileImage().getImageLink() : null)
        .isMutualFollow(isMutualFollow ? true : null)
        .build();
  }

  /**
   * 팔로워 유저 정보를 FollowerResponseDTO로 변환
   */
  public static FollowDTO.FollowerResponseDTO toFollowerResponseDTO(UserEntity userEntity, Boolean isMutualFollow) {
    return FollowDTO.FollowerResponseDTO.builder()
        .name(userEntity.getName())
        .nickname(userEntity.getNickname())
        .profileImage(userEntity.getProfileImage() != null ? userEntity.getProfileImage().getImageLink() : null)
        .isMutualFollow(isMutualFollow ? true : null)
        .build();
  }
}
