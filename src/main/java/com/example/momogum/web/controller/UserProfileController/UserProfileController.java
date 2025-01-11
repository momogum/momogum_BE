package com.example.momogum.web.controller.UserProfileController;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.service.UserService;
import com.example.momogum.web.dto.MealDiaryDTO;
import com.example.momogum.web.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/UserProfile")
@Tag(name = "UserProfile API",description = "유저프로필 API")
public class UserProfileController {
  private final UserService userService;

  /**
   * 유저 프로필 기능
   *
   * 1. 유저 정보 조회 (유저 닉네임, 이름, 팔로워, 팔로잉 조회)
   *    -> 유저 닉네임 핸들러 설계 나중에
   * 2. 팔로워/팔로잉 전부 조회 (페이징 X)
   * 3. 프로필 편집 기능
   * 4. 밥일기 조회 (월 별 페이징)
   * 5. 또 오고 싶어요 기능?
   * 6. 설정 편집
   */

  /**
   *  유저 정보 조회
   *  1. 유저 닉네임, 실명, 팔로워/팔로잉(총 인원), 프로필 사진 조회
   *  2. 한 줄 소개
   */

  @Operation(summary = "유저 정보 조회 API",description= " 유저의 기본 정보를 반환")
  @GetMapping("/{nickname}")
  public ApiResponse<UserDTO.Response> getUserProfile(@PathVariable String nickname) {
    return userService.getUserByNickname(nickname)
        .map(user->ApiResponse.onSuccess(UserDTO.Response.builder()
            .id(user.getId())
            .nickName(user.getNickname())             // 유저 닉네임
            .name(user.getName())                     // 유저 실명
            .bio(user.getBio())                       // 한줄 소개
            .profileImage(user.getProfileImage())     // 프로필 이미지
            // 유저에 필드로 넣을지 서비스 레이어에서 계산할 지 모르겠습니다
            .followersCount(user.getFollowersCount())
            .followingsCount(user.getFollowingsCount())
            .build())
        )
  }

  /**
   * 팔로워 조회
   */

  @Operation(summary = "팔로워 조회", description = "유저의 팔로워 목록 반환")
  @GetMapping("/{nickname}/followers")
  public ApiResponse<List<UserDTO.Response>> getFollowers(@PathVariable String nickname) {
    return userService.getUserByNickname(nickname)
        .map(user -> ApiResponse.onSuccess(userService.getFollowers(user.getId())))
        .orElseGet(() -> ApiResponse.onFailure("유저를 찾을 수 없습니다.", 404));
  }

  /**
   * 팔로잉 조회
   */
  @Operation(summary = "팔로잉 조회", description = "유저가 팔로우하는 유저 목록 반환")
  @GetMapping("/{nickName}/followings")
  public ApiResponse<List<UserDTO.Response>> getFollowings(@PathVariable String nickName) {
    return userService.getUserBynickName(nickName)
        .map(user -> ApiResponse.onSuccess(userService.getFollowings(user.getId())))
        .orElseGet(() -> ApiResponse.onFailure("유저를 찾을 수 없습니다.", 404));
  }

  /**
   * 프로필 편집
   * 1. 유저 프로필 편집 (닉네임, 실명, bio, 프사)
   */
  @Operation(summary = "프로필 수정", description = "유저 프로필 정보 수정.")
  @PatchMapping("/{nickname}/profile")
  public ApiResponse<UserDTO.Response> editUserProfile(
      @PathVariable String nickname,
      @RequestBody UserDTO.Edit editDTO) {
    return userService.editUserProfile(Nickname, editDTO)
        .map(user -> ApiResponse.onSuccess(UserDTO.Response.builder()
            .id(user.getId())
            .nickName(user.getNickname())
            .name(user.getName())
            .bio(user.getBio())
            .profileImage(user.getProfileImage())
            .followersCount(userService.countFollowers(user.getId())) // 동기화된 followers 수 반환
            .followingCount(userService.countFollowings(user.getId()))
            .build()));
  }

  /**
   * 월별 밥일기 조회
   * 0. 밥일기 테이블에서 월별로 페이징
   * 1. 월별로 조회 가능
   */

  @Operation(summary = "유저프로필 밥일기 월별 조회", description = "밥일기 월별 페이징")
  @GetMapping("/{nickname}/mealDiary/{year}/{month}")
  public ApiResponse<MealDiaryDTO.Response> getMonthlyDiaryImages(
      @PathVariable String nickname,
      @PathVariable int year,
      @PathVariable int month) {
    MealDiaryDTO.Response response = mealDiaryService.getMonthlyDiaryImages(nickname, year, month);
    return ApiResponse.onSuccess(response);
  }

  /**
   * 밥일기 상세 정보 조회
   * 0. 월별로 페이징된 밥일기 클릭
   * 1. 밥일기 상세정보 파악 가능
   */

  @Operation(summary = "유저프로필 밥일기 조회",description = "밥일기 상세 정보 조회")
  @GetMapping("/{nickname}/mealDiary/{mealDiaryId}")
  public ApiResponse<MealDiaryDTO.Detail> getMealDiaryDetail(
      @PathVariable String nickname,
      @PathVariable Long mealDiaryId) {
    MealDiaryDTO.Detail response = mealDiaryService.getMealDiaryDetail(nickname, mealDiaryId);
    return ApiResponse.onSuccess(response);
  }

  /**
   * 또 오고 싶어요 기능
   * 0. 어떤 기능인지 파악이 안됩니다..
   */


}
