package com.example.momogum.web.controller.UserProfileController;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.service.userProfileService.FollowServiceImpl;
import com.example.momogum.service.userProfileService.TargetProfileServiceImpl;
import com.example.momogum.service.userProfileService.UserProfileService;
import com.example.momogum.service.userProfileService.UserProfileServiceImpl;
import com.example.momogum.web.dto.user.UserDTO;
import com.example.momogum.web.dto.user.UserDTO.FullProfileDTO;
import com.example.momogum.web.dto.viewMealDiary.ViewMealDiaryDTO;
import com.example.momogum.web.dto.viewMealDiary.ViewMealDiaryDTO.ViewMealDiaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/targets")
@RequiredArgsConstructor
@Tag(name = "TargetProfile API",description = "타겟 프로필 API")
public class TargetProfileController {

  private final UserProfileServiceImpl userProfileServiceImpl;
  private final TargetProfileServiceImpl targetProfileServiceImpl;
  private final FollowServiceImpl followService;

  /**
   * 상대 유저의 기본 프로필 정보 조회 API
   */
  @Operation(summary = "상대 프로필 조회 API", description = "특정 사용자의 프로필 정보를 조회합니다.")
  @GetMapping("/{targetUserId}/profile")
  public ApiResponse<UserDTO.FullProfileDTO> getTargetProfile(
      @PathVariable Long targetUserId,
      // X-User-Id로 현재 로그인한 사용자의 ID를 서버에 전달
      @RequestHeader("X-User-Id") Long currentUserId
  ) {
    boolean isFollowing = followService.isFollowing(currentUserId, targetUserId);
    UserDTO.FullProfileDTO targetProfile = targetProfileServiceImpl.getTargetProfile(targetUserId, isFollowing);
    return ApiResponse.onSuccess(targetProfile);
  }

  /**
   * 상대 유저의 밥일기 목록 조회 API
   */
  @Operation(summary = "상대가 작성한 밥일기 목록 조회 API", description = "특정 사용자가 작성한 밥일기 목록을 조회합니다.")
  @GetMapping("/{targetUserId}/meal-diaries")
  public ApiResponse<List<ViewMealDiaryResponse>> getTargetMealDiaries(
      @PathVariable Long targetUserId
  ) {
    List<ViewMealDiaryResponse> mealDiaries = targetProfileServiceImpl.getTargetMealDiaries(targetUserId);
    return ApiResponse.onSuccess(mealDiaries);
  }


}
