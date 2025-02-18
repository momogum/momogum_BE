package com.example.momogum.web.controller.UserProfileController;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.service.userProfileService.FollowService;
import com.example.momogum.service.userProfileService.UserProfileServiceImpl;
import com.example.momogum.service.userProfileService.ProfileImageService;
import com.example.momogum.web.dto.FollowDTO;
import com.example.momogum.web.dto.user.ProfileImageDTO;
import com.example.momogum.web.dto.user.UserDTO;
import com.example.momogum.web.dto.viewMealDiary.ViewMealDiaryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import java.io.FileNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/userProfiles")
@RequiredArgsConstructor
@Tag(name = "유저프로필 API",description = "유저프로필 API")
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

    UserDTO.UserResponseDTO getUserProfile = userProfileServiceImpl.getUserProfile(userId);

    return ApiResponse.onSuccess(getUserProfile);
  }

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


  @Operation(summary= "유저 프로필 이미지 수정 API", description =  "유저 프로필 이미지를 수정하고 수정된 정보를 반환합니다.")
  @PostMapping(path= "/profileImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ApiResponse<List<String>> updateUserProfileImage(
      @RequestParam(value = "userId") Long userId,  @RequestPart(value = "files") List<MultipartFile> files) {

    String dirname = "user-profile-images";

    // 프로필 이미지 업로드 호출
    // 반환값 수정해야함 FIXME
    List<String> updatedImage = profileImageService.uploadImages(
        files, dirname,userId);

    return ApiResponse.onSuccess(updatedImage);
  }
  */

  /**
   * 프로필 이미지 기본 이미지로 수정
   */
  @Operation(summary = "기본 프로필 이미지로 변경", description = "모든 경우에서 기본 프로필 이미지로 변경")
  @PutMapping("/{userId}/setDefaultProfileImage")
  public ApiResponse<String> setDefaultProfileImage(@PathVariable Long userId) {
    String defaultImageUrl = profileImageService.setDefaultProfileImage(userId);
    return ApiResponse.onSuccess(defaultImageUrl);
  }

  /**
   * 프로필 이미지 커스텀 이미지로 수정
   */
  @Operation(summary = "갤러리 이미지로 변경", description = "사용자가 직접 업로드한 이미지로 프로필을 변경")
  @PutMapping(value = "/{userId}/uploadCustomProfileImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ApiResponse<String> uploadCustomProfileImage(
      @PathVariable Long userId,
      @RequestPart("file") @Parameter(description = "업로드할 커스텀 프로필 이미지") MultipartFile file) {

    String updatedImageUrl = profileImageService.uploadCustomProfileImage(file, userId);
    return ApiResponse.onSuccess(updatedImageUrl);
  }

  /**
   * 내가 작성한 밥일기 목록 조회 API
   */

  @Operation(summary = "작성한 밥일기 목록 조회 API", description = "본인이 작성한 밥일기의 목록을 조회합니다.")
  @GetMapping("/{userId}/meal-diaries")
  public ApiResponse<List<ViewMealDiaryDTO.ViewMealDiaryResponse>> getUserMealDiaries(@PathVariable Long userId) {
    List<ViewMealDiaryDTO.ViewMealDiaryResponse> response = userProfileServiceImpl.getUserMealDiaries(userId);
    return ApiResponse.onSuccess(response);
  }

  @Operation(summary = "북마크한 밥일기 목록 조회 API", description = "북마크 해놓은 밥일기의 목록을 조회합니다.")
  @GetMapping("/{userId}/bookmarked-meal-diaries")
  public ApiResponse<List<ViewMealDiaryDTO.ViewMealDiaryResponse>> getBookmarkedMealDiaries(@PathVariable Long userId) {
    List<ViewMealDiaryDTO.ViewMealDiaryResponse> response = userProfileServiceImpl.getBookmarkedMealDiaries(userId);
    return ApiResponse.onSuccess(response);
  }

  /**
  @Operation(summary = "이미지 삭제 API(테스트 용)")
  @DeleteMapping("")
  public ApiResponse<String> deleteUserProfile(@RequestParam Long userId)
      throws FileNotFoundException {

    profileImageService.deleteImage(userId);
    return ApiResponse.onSuccess("aa");
  }

  @Operation(summary = "기본 이미지 조회 API(테스트 용)")
  @GetMapping("")
  public String getafads(){
    return profileImageService.viewProfileImage();
  }
  */

}




