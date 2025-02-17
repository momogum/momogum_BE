package com.example.momogum.web.controller.UserProfileController;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.service.searchService.SearchServiceImpl;
import com.example.momogum.service.userProfileService.FollowService;
import com.example.momogum.web.dto.FollowDTO;
import com.example.momogum.web.dto.FollowDTO.FollowingResponseDTO;
import com.example.momogum.web.dto.search.SearchDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/follows")
@RequiredArgsConstructor
@Tag(name = "팔로우 API",description = "팔로잉 팔로우와 관련된 API")
public class FollowController {

  private final FollowService followService;
  private final SearchServiceImpl searchServiceImpl;

  /**
   * 팔로우 추가 토글
   */
  //FIXME : Header보다 PathVariable 사용해서 작성

  @Operation(summary = "팔로우 추가 토글 API", description = "팔로우 등록 API 입니다.<br>"
      +"경로 변수(`userId`, `targetUserId`)를 통해 요청하시면 됩니다.<br>"
      +"한 번 클릭하면 팔로우, 두 번 클릭하면 언팔로우됩니다.")
  @PostMapping("{userId}/follow/{targetUserId}/toggle")
  public ApiResponse<String> toggleFollow(
      @PathVariable Long userId, @PathVariable Long targetUserId // 팔로우 대상
  ) {
    followService.toggleFollowUser(userId, targetUserId);
    return ApiResponse.onSuccess("팔로우 상태가 변경되었습니다.");
  }

  /**
   * 팔로워 삭제 토글
   */
  //FIXME : 헤더보다 PathVariable 사용

  @Operation(summary = "팔로워 삭제 토글 API", description = "팔로워 삭제 API 입니다.<br>\"\n"
      + "      +\"경로 변수(`userId`, `followerId`)를 통해 요청하시면 됩니다.<br>\"\n"
      + "      +\"클릭 시에 팔로워를 삭제합니다."
    +"바디에 삭제할 팔로워 ID를 넣어서 요청하시면 됩니다.")
  @DeleteMapping("{userId}/delete/{followerId}/toggle")
  public ApiResponse<String> removeFollower(
      @PathVariable Long userId, @PathVariable Long followerId // 삭제할 팔로워
  ){
    followService.removeFollower(userId, followerId);
    return ApiResponse.onSuccess("팔로워가 성공적으로 삭제되었습니다.");
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
  @GetMapping("/{userId}/search/following")
  public ApiResponse<List<FollowDTO.FollowingResponseDTO>> getAllFollowing(@PathVariable Long userId) {

    return ApiResponse.onSuccess(followService.getFollowings(userId));
  }


  /**
   * 팔로워 목록 조회
   */

  @Operation(summary = "팔로워 목록 조회 API", description = "유저의 팔로워(나를 팔로우하는 사람들) 조회")
  @GetMapping("/{userId}/search/followers")
  public ApiResponse<List<FollowDTO.FollowerResponseDTO>> getAllFollowers(@PathVariable Long userId) {

    return ApiResponse.onSuccess(followService.getFollowers(userId));
  }

  /**
   * 닉네임 또는 이름으로 팔로잉하는 유저 검색
   */
  @Operation(summary = "닉네임 또는 이름으로 팔로잉 검색", description = "나를 팔로잉하는 유저의 닉네임 or 이름 검색")
  @GetMapping("/{userId}/search/followings/name")
  public ApiResponse<List<SearchDTO.FollowingSearchResponseDTO>> getFollowigsSearch(
      //현재 사용자 아이디
      @PathVariable Long userId,
      //검색할 사용자 name or nickname
      @RequestParam String query) {

    List<SearchDTO.FollowingSearchResponseDTO> result = searchServiceImpl.getFollowingsSearch(userId, query);
    return ApiResponse.onSuccess(result);
  }

  /**
   * 닉네임 또는 이름으로 나를 팔로우한 유저 검색
   */
  @Operation(summary = "닉네임 또는 이름으로 팔로워 검색", description = "내가 팔로우하는 유저의 닉네임 or 이름 검색")
  @GetMapping("/{userId}/search/followers/name")
  public ApiResponse<List<SearchDTO.FollowerSearchResponseDTO>> getFollowersSearch(
      @PathVariable Long userId,
      @RequestParam String query) {

    List<SearchDTO.FollowerSearchResponseDTO> result = searchServiceImpl.getFollowersSearch(userId, query);
    return ApiResponse.onSuccess(result);
  }

}
