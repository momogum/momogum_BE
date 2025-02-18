package com.example.momogum.web.controller.UserProfileController;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.GeneralException;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.service.userProfileService.FollowServiceImpl;
import com.example.momogum.service.userProfileService.TargetProfileServiceImpl;
import com.example.momogum.service.userProfileService.UserProfileService;
import com.example.momogum.service.userProfileService.UserProfileServiceImpl;
import com.example.momogum.web.dto.FollowDTO;
import com.example.momogum.web.dto.user.UserDTO;
import com.example.momogum.web.dto.user.UserReportDTO;
import com.example.momogum.web.dto.viewMealDiary.ViewMealDiaryDTO.ViewMealDiaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/targets")
@RequiredArgsConstructor
@Tag(name = "TargetProfile API",description = "타겟 프로필 API")
public class TargetProfileController {

  private final TargetProfileServiceImpl targetProfileServiceImpl;

  /**
   * 상대 유저의 기본 프로필 정보 조회 API
   */
  @Operation(summary = "상대 프로필 조회 API", description = "특정 사용자의 프로필 정보를 조회합니다.")
  @GetMapping("{userId}/target/{targetUserId}/profile")
  public ApiResponse<UserDTO.FullProfileDTO> getTargetProfile(
      @PathVariable Long userId,
      @PathVariable Long targetUserId
  ) {
    UserDTO.FullProfileDTO targetProfile = targetProfileServiceImpl.getTargetProfile(userId, targetUserId);
    return ApiResponse.onSuccess(targetProfile);
  }

  /**
   * 상대 유저의 밥일기 목록 조회 API
   */
  @Operation(summary = "상대가 작성한 밥일기 목록 조회 API", description = "상대 유저가 작성한 밥일기 목록을 조회합니다.")
  @GetMapping("/{userId}/target/{targetUserId}/meal-diaries")
  public ApiResponse<List<ViewMealDiaryResponse>> getTargetMealDiaries(
      @PathVariable Long userId,
      @PathVariable Long targetUserId
  ) {
    List<ViewMealDiaryResponse> mealDiaries = targetProfileServiceImpl.getTargetMealDiaries(userId, targetUserId);
    return ApiResponse.onSuccess(mealDiaries);
  }

  /**
   * 상대 유저가 저장한 밥일기 목록 조회 API
   */
  @Operation(summary = "상대방이 저장한 밥일기 목록 조회 API", description = "상대 유저가 북마크한 밥일기 목록을 조회합니다.")
  @GetMapping("/{userId}/target/{targetUserId}/bookmarked")
  public ApiResponse<List<ViewMealDiaryResponse>> getTargetBookmarkedMealDiaries(
      @PathVariable Long userId,
      @PathVariable Long targetUserId
  ) {
    List<ViewMealDiaryResponse> bookmarkedMealDiaries = targetProfileServiceImpl.getTargetBookmarkedMealDiaries(userId, targetUserId);
    return ApiResponse.onSuccess(bookmarkedMealDiaries);
  }
  /**
   * 상대 유저의 팔로잉 목록 조회 API
   */
  @Operation(summary = "상대방의 팔로잉 목록 조회 API", description = "상대 유저가 팔로우하고 있는 사람 목록을 조회합니다.")
  @GetMapping("{userId}/target/{targetUserId}/following")
  public ApiResponse<List<FollowDTO.FollowingResponseDTO>> getTargetFollowings(
      @PathVariable Long userId,
      @PathVariable Long targetUserId
  ) {
    List<FollowDTO.FollowingResponseDTO> followingList = targetProfileServiceImpl.getTargetFollowings(targetUserId);
    return ApiResponse.onSuccess(followingList);
  }

  /**
   * 상대 유저의 팔로워 목록 조회 API
   */
  @Operation(summary = "상대방의 팔로워 목록 조회 API", description = "상대 유저를 팔로우하고 있는 사람 목록을 조회합니다.")
  @GetMapping("{userId}/target/{targetUserId}/followers")
  public ApiResponse<List<FollowDTO.FollowerResponseDTO>> getTargetFollowers(
      @PathVariable Long userId,
      @PathVariable Long targetUserId
  ) {
    List<FollowDTO.FollowerResponseDTO> followerList = targetProfileServiceImpl.getTargetFollowers(targetUserId);
    return ApiResponse.onSuccess(followerList);
  }

  /**
   * 상대 유저 신고하기 API
   */
  @Operation(summary = "상대 유저 신고하기 API", description = "부적절한 상대 유저를 신고합니다.")
  @PostMapping("{userId}/target/report")
  public ApiResponse<UserReportDTO.UserReportResponseDTO> report(
      @PathVariable Long userId,
      @RequestBody UserReportDTO.UserReportRequestDTO request) {

    UserReportDTO.UserReportResponseDTO result = targetProfileServiceImpl.report(userId,request);
    return ApiResponse.onSuccess(result);
  }

}
