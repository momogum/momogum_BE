package com.example.momogum.web.controller.UserProfileController;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.GeneralException;
import com.example.momogum.service.UserProfileService;
import com.example.momogum.web.dto.FollowDTO;
import com.example.momogum.web.dto.FollowDTO.FollowStatsDTO;
import com.example.momogum.web.dto.UserDTO;
import com.example.momogum.web.dto.user.UserEditDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/UserProfile")
@RequiredArgsConstructor
@Tag(name = "UserProfile API",description = "유저프로필 API")
public class UserProfileController {

  private final UserProfileService userProfileService;
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
  public ApiResponse<UserDTO.Response> getUserProfile(@PathVariable Long userId) {

    UserDTO.Response userProfile = userProfileService.getUserProfile(userId);
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
   * 유저 프로필 수정 API
   */

  @Operation(summary = "유저 프로필 수정 API", description = "유저 프로필을 수정하고 수정된 정보를 반환합니다.")
  @PutMapping("/{userId}/profile")
  public ApiResponse<UserEditDTO.Response> updateUserProfile(
      @PathVariable Long userId, @RequestBody @Valid UserEditDTO.Request request) {

    UserEditDTO.Response updatedProfile = userProfileService.updateUserProfile(userId, request);

    return ApiResponse.onSuccess(updatedProfile);
  }

}

  /**
   *
   */



//  /**
//   * 팔로워 조회
//   */
//
//  @Operation(summary = "팔로워 조회", description = "유저의 팔로워 목록 반환")
//  @GetMapping("/{nickname}/followers")
//  public ApiResponse<List<UserDTO.Response>> getFollowers(@PathVariable String nickname) {
//
//    // 임시 데이터 생성
//    List<UserDTO.Response> followers = new ArrayList<>();
//    followers.add(UserDTO.Response.builder()
//            .id(1L)
//            .nickname("follower1")
//            .name("John Doe")
//            .bio("This is a bio of follower1")
//            .profileImage("https://example.com/profile1.jpg")
//            .followersCount(10)
//            .followingCount(5)
//            .build());
//
//    followers.add(UserDTO.Response.builder()
//            .id(2L)
//            .nickname("follower2")
//            .name("Jane Doe")
//            .bio("This is a bio of follower2")
//            .profileImage("https://example.com/profile2.jpg")
//            .followersCount(15)
//            .followingCount(8)
//            .build());
//
//    return ApiResponse.onSuccess(followers);
//  }
//
////    return userService.getUserByNickname(nickname)
////        .map(user -> ApiResponse.onSuccess(userService.getFollowers(user.getId())))
////        .orElseGet(() -> ApiResponse.onFailure("유저를 찾을 수 없습니다.", 404));
////  }
//
//  /**
//   * 팔로잉 조회
//   */
//  @Operation(summary = "팔로잉 조회", description = "유저가 팔로우하는 유저 목록 반환")
//  @GetMapping("/{nickName}/followings")
//  public ApiResponse<List<UserDTO.Response>> getFollowings(@PathVariable String nickName) {
//    // 임시 데이터 생성
//    List<UserDTO.Response> followers = new ArrayList<>();
//    followers.add(UserDTO.Response.builder()
//            .id(1L)
//            .nickname("follower1")
//            .name("John Doe")
//            .bio("This is a bio of follower1")
//            .profileImage("https://example.com/profile1.jpg")
//            .followersCount(10)
//            .followingCount(5)
//            .build());
//
//    followers.add(UserDTO.Response.builder()
//            .id(2L)
//            .nickname("follower2")
//            .name("Jane Doe")
//            .bio("This is a bio of follower2")
//            .profileImage("https://example.com/profile2.jpg")
//            .followersCount(15)
//            .followingCount(8)
//            .build());
//
//    return ApiResponse.onSuccess(followers);
//  }
//
//  /**
//   * 프로필 편집
//   * 1. 유저 프로필 편집 (닉네임, 실명, bio, 프사)
//   */
//  @Operation(summary = "프로필 수정", description = "유저 프로필 정보 수정.")
//  @PatchMapping("/{nickname}/profile")
//  public ApiResponse<UserDTO.Response> editUserProfile(
//      @PathVariable String nickname,
//      @RequestBody UserDTO.Edit editDTO) {
//    return ApiResponse.onSuccess(UserDTO.Response.builder()
//            .id(1L)
//            .nickname("temp")
//            .name("temp")
//            .bio("temp")
//            .profileImage("temp")
//            .followersCount(0)
//            .followingCount(0)
//            .build());}
//
//  /**
//   * 월별 밥일기 조회
//   * 0. 밥일기 테이블에서 월별로 페이징
//   * 1. 월별로 조회 가능
//   */
//
//    @Operation(summary = "유저프로필 밥일기 월별 조회", description = "밥일기 월별 페이징")
//    @GetMapping("/{nickname}/mealDiary/{year}/{month}")
//    public void getMonthlyDiaryImages(
//            @PathVariable String nickname,
//    @PathVariable int year,
//    @PathVariable int month) {
//
//    }
//
//  /**
//   * 밥일기 상세 정보 조회
//   * 0. 월별로 페이징된 밥일기 클릭
//   * 1. 밥일기 상세정보 파악 가능
//   */
//
//  @Operation(summary = "유저프로필 밥일기 조회",description = "밥일기 상세 정보 조회")
//  @GetMapping("/{nickname}/mealDiary/{mealDiaryId}")
//  public void getMealDiaryDetail(
//      @PathVariable String nickname,
//      @PathVariable Long mealDiaryId) {
//  }
//
//  /**
//   * 또 오고 싶어요 기능
//   * 0. 어떤 기능인지 파악이 안됩니다..
//   */



