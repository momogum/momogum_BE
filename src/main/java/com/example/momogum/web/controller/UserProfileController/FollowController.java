package com.example.momogum.web.controller.UserProfileController;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.service.userProfileService.FollowService;
import com.example.momogum.web.dto.FollowDTO;
import com.example.momogum.web.dto.FollowDTO.FollowingResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
@Tag(name = "Follow API",description = "팔로잉 팔로우 API")
public class FollowController {
  private final FollowService followService;

  /**
   * 닉네임 또는 이름으로 팔로잉하는 유저 검색
   */
  @Operation(summary = "닉네임 또는 이름으로 팔로잉 검색", description = "나를 팔로잉하는 유저의 닉네임 or 이름 검색")
  @GetMapping("/followings/search")
  public ApiResponse<List<FollowDTO.FollowingResponseDTO>> searchFollowingsByQuery(
      //현재 사용자 아이디
      @RequestParam Long userId,
      //검색할 사용자 name or nickname
      @RequestParam String query) {

    List<FollowingResponseDTO> result = followService.searchFollowingsByQuery(userId, query);
    return ApiResponse.onSuccess(result);
  }

  /**
   * 닉네임 또는 이름으로 나를 팔로우한 유저 검색
   */
  @Operation(summary = "닉네임 또는 이름으로 팔로워 검색", description = "내가 팔로우하는 유저의 닉네임 or 이름 검색")
  @GetMapping("/followers/search")
  public ApiResponse<List<FollowDTO.FollowerResponseDTO>> searchFollowersByQuery(
      @RequestParam Long userId,
      @RequestParam String query) {

    List<FollowDTO.FollowerResponseDTO> result = followService.searchFollowersByQuery(userId, query);
    return ApiResponse.onSuccess(result);
  }

}
