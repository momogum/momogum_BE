package com.example.momogum.service.userProfileService;

import com.example.momogum.web.dto.FollowDTO;
import com.example.momogum.web.dto.user.UserDTO;
import com.example.momogum.web.dto.viewMealDiary.ViewMealDiaryDTO;
import com.example.momogum.web.dto.viewMealDiary.ViewMealDiaryDTO.ViewMealDiaryResponse;
import java.util.List;

public interface TargetProfileService {

  UserDTO.FullProfileDTO getTargetProfile(Long targetUserId, boolean isFollowing);

  List<ViewMealDiaryResponse> getTargetMealDiaries(Long targetUserId);

  List<ViewMealDiaryDTO.ViewMealDiaryResponse> getTargetBookmarkedMealDiaries(Long targetUserId);

  List<FollowDTO.FollowingResponseDTO> getTargetFollowings(Long currentUserId, Long targetUserId);

  List<FollowDTO.FollowerResponseDTO> getTargetFollowers(Long currentUserId, Long targetUserId);
}
