package com.example.momogum.service.userProfileService;

import com.example.momogum.web.dto.FollowDTO;
import com.example.momogum.web.dto.user.UserDTO;
import com.example.momogum.web.dto.user.UserReportDTO;
import com.example.momogum.web.dto.viewMealDiary.ViewMealDiaryDTO;
import com.example.momogum.web.dto.viewMealDiary.ViewMealDiaryDTO.ViewMealDiaryResponse;
import java.util.List;

public interface TargetProfileService {

  UserDTO.FullProfileDTO getTargetProfile(Long currentUserId, Long targetUserId);

  List<ViewMealDiaryResponse> getTargetMealDiaries(Long currentUserId, Long targetUserId);

  List<ViewMealDiaryResponse> getTargetBookmarkedMealDiaries(Long currentUserId, Long targetUserId);

  List<FollowDTO.FollowingResponseDTO> getTargetFollowings(Long targetUserId);

  List<FollowDTO.FollowerResponseDTO> getTargetFollowers(Long targetUserId);

  UserReportDTO.UserReportResponseDTO report(Long reporterId, UserReportDTO.UserReportRequestDTO request);
}
