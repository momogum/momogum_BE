package com.example.momogum.service.userProfileService;

import com.example.momogum.web.dto.FollowDTO;
import com.example.momogum.web.dto.user.UserDTO;

public interface UserProfileService {
  // 유저 프로필 조회

  UserDTO.UserResponseDTO getUserProfile(Long userId);

  // 팔로워/팔로잉 수 조회

  FollowDTO.FollowStatsDTO getFollowStats(Long userId);

  // 유저 프로필 수정
  UserDTO.UserEditDTO updateUserProfile(Long userId, UserDTO.UserEditDTO request);

}
