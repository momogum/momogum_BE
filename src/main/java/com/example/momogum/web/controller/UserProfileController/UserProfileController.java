package com.example.momogum.web.controller.UserProfileController;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.service.UserProfileService;
import com.example.momogum.service.ProfileImageService;
import com.example.momogum.web.dto.FollowDTO;
import com.example.momogum.web.dto.user.ProfileImageDTO;
import com.example.momogum.web.dto.user.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/UserProfile")
@RequiredArgsConstructor
@Tag(name = "UserProfile API",description = "유저프로필 API")
public class UserProfileController {

  private final UserProfileService userProfileService;
  private final ProfileImageService profileImageService;

  /**
   * 유저 프로필 기능
   *
   * 1. 유저 정보 조회 (유저 닉네임, 이름, 팔로워, 팔로잉 조회)
   *    -> 유저 닉네임 핸들러 설계 나중에
   * 2. 팔로워/팔로잉 전부 조회 (페이징 X)
   * 3. 프로필 편집 기능
   * 4. 밥일기 조회 (월 별 페이징)
   * 5. 또 오고 싶어요 기능?
   */

  /**
   * 유저 정보 조회 1. 유저 닉네임, 실명, 팔로워/팔로잉(총 인원), 프로필 사진 조회 2. 한 줄 소개
   */

  @Operation(summary = "유저 정보 조회 API", description = " 유저의 기본 정보를 반환")
  @GetMapping("/{userId}")
  public ApiResponse<UserDTO.UserResponseDTO> getUserProfile(@PathVariable Long userId) {

    UserDTO.UserResponseDTO userProfile = userProfileService.getUserProfile(userId);
    return ApiResponse.onSuccess(userProfile);
  }


  /**
   * 팔로워/ 팔로잉 수 조회
   */
  @Operation(summary = "팔로워/팔로잉 수 카운트 API", description = "팔로워/팔로잉 수 카운트 반환")
  @GetMapping("/{userId}/follows")
  public ApiResponse<FollowDTO.FollowStatsDTO> getFollowStats(@PathVariable Long userId) {

    FollowDTO.FollowStatsDTO followStats = userProfileService.getFollowStats(userId);

    return ApiResponse.onSuccess(followStats);
  }

  /**
   * 팔로워/ 팔로잉 멤버 조회
   */



  /**
   * 유저 프로필 기본 정보 수정 API
   */

  @Operation(summary = "유저 프로필 수정 API", description = "유저 프로필을 수정하고 수정된 정보를 반환합니다.")
  @PutMapping("/{userId}/profile")
  public ApiResponse<UserDTO.UserEditDTO> updateUserProfile(
      @PathVariable Long userId, @RequestBody @Valid UserDTO.UserEditDTO request) {

    UserDTO.UserEditDTO updatedProfile = userProfileService.updateUserProfile(userId, request);

    return ApiResponse.onSuccess(updatedProfile);
  }


  /**
   * 유저 프로필 이미지 수정 API
   */

  @Operation(summary= "유저 프로필 이미지 수정 API", description =  "유저 프로필 이미지를 수정하고 수정된 정보를 반환합니다.")
  @PutMapping("/{userId}/profileImage")
  public ApiResponse<ProfileImageDTO.ProfileImageResponseDTO> updateUserProfileImage(
      @PathVariable Long userId, @RequestPart(required = false) MultipartFile file) {

    // 프로필 이미지 업로드 호출
    ProfileImageDTO.ProfileImageResponseDTO updatedImage = profileImageService.uploadProfileImage(
        file, userId);

    return ApiResponse.onSuccess(updatedImage);
  }
}




