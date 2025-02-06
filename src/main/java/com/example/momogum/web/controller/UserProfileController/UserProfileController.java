package com.example.momogum.web.controller.UserProfileController;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.service.userProfileService.FollowService;
import com.example.momogum.service.userProfileService.UserProfileServiceImpl;
import com.example.momogum.service.userProfileService.ProfileImageService;
import com.example.momogum.web.dto.FollowDTO;
import com.example.momogum.web.dto.user.ProfileImageDTO;
import com.example.momogum.web.dto.user.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/userProfiles")
@RequiredArgsConstructor
@Tag(name = "UserProfile API",description = "유저프로필 API")
public class UserProfileController {

  private final UserProfileServiceImpl userProfileServiceImpl;
  private final ProfileImageService profileImageService;
  private final FollowService followService;

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
  @GetMapping("/userId/{userId}")
  public ApiResponse<UserDTO.UserResponseDTO> getUserProfile(@PathVariable Long userId) {

    UserDTO.UserResponseDTO userProfile = userProfileServiceImpl.getUserProfile(userId);
    return ApiResponse.onSuccess(userProfile);
  }

  /**
   * 팔로잉, 팔로워 수 카운트
   */

  @Operation(summary = "팔로워/팔로잉 수 조회 API", description = "특정 유저의 팔로워 및 팔로잉 수를 조회합니다.")
  @GetMapping("/{userId}/followCount")
  public ApiResponse<FollowDTO.FollowStatsDTO> getFollowStats(@PathVariable Long userId) {
    return ApiResponse.onSuccess(followService.getFollowStats(userId));
  }

  /**
   * 팔로잉 목록 조회
   */

  @Operation(summary = "팔로잉 목록 조회 API", description = "유저의 팔로잉(내가 팔로우하는 사람들) 조회")
  @GetMapping("/{userId}/following")
  public ApiResponse<List<FollowDTO.FollowingResponseDTO>> getAllFollowing(@PathVariable Long userId) {

    return ApiResponse.onSuccess(followService.getFollowings(userId));
  }


  /**
   * 팔로워 목록 조회
   */

  @Operation(summary = "팔로워 목록 조회 API", description = "유저의 팔로워(나를 팔로우하는 사람들) 조회")
  @GetMapping("/{userId}/followers")
  public ApiResponse<List<FollowDTO.FollowerResponseDTO>> getAllFollowers(@PathVariable Long userId) {

    return ApiResponse.onSuccess(followService.getFollowers(userId));
  }

//  /**
//  * 팔로잉, 팔로워 토글 (언팔로우, 팔로우)
//  */
//
//  @Operation(summary = "팔로우 토글 API",
//             description = "팔로우 등록 API<br>"+"팔로우 대상 PathVariable에 담아서 전달<br>"+"1번 클릭 팔로우 등록, 2번 클릭 : 언팔로우")
//
//  @PostMapping("/{userId}/follow/{targetUserId}")
//  public ApiResponse<String> follow(@PathVariable Long userId, @PathVariable Long targetUserId) {
//
//
//  }

  /**
   * 유저 프로필 기본 정보 수정 API
   */

  @Operation(summary = "유저 프로필 수정 API", description = "유저 프로필을 수정하고 수정된 정보를 반환합니다.")
  @PutMapping("/{userId}/profile")
  public ApiResponse<UserDTO.UserEditDTO> updateUserProfile(
      @PathVariable Long userId, @RequestBody @Valid UserDTO.UserEditDTO request) {

    UserDTO.UserEditDTO updatedProfile = userProfileServiceImpl.updateUserProfile(userId, request);

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

  /**
   * 상대 프로필 조회 API
   */

  @Operation(summary = "상대 프로필 조회 API", description = "상대 프로필의 전체를 조회합니다.")
  @GetMapping("{userId}/fullProfile")
  public  ApiResponse<UserDTO.FullProfileDTO> getFullProfile(@PathVariable Long userId) {
    return ApiResponse.onSuccess(userProfileServiceImpl.getFullProfile(userId));
  }

}




