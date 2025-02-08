package com.example.momogum.service.userProfileService;

import com.example.momogum.web.dto.FollowDTO;
import com.example.momogum.web.dto.user.UserDTO;
import com.example.momogum.web.dto.viewMealDiary.ViewMealDiaryDTO;
import com.example.momogum.web.dto.viewMealDiary.ViewMealDiaryDTO.ViewMealDiaryResponse;
import java.util.List;

public interface UserProfileService {
  // 유저 프로필 조회

  UserDTO.UserResponseDTO getUserProfile(Long userId);

  // 팔로워/팔로잉 수 조회

  // FollowDTO.FollowStatsDTO getFollowStats(Long userId);

  // 유저 프로필 수정
  UserDTO.UserEditDTO updateUserProfile(Long userId, UserDTO.UserEditDTO request);

  // 상대 유저 프로필 조회
  UserDTO.FullProfileDTO getFullProfile(Long userId);

  // 작성한,북마크한 밥일기 조회
  List<ViewMealDiaryDTO.ViewMealDiaryResponse> getUserMealDiaries(Long userId);
  List<ViewMealDiaryResponse> getBookmarkedMealDiaries(Long userId);

}
