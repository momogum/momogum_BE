package com.example.momogum.web.controller.UserProfileController;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.service.userProfileService.UserProfileService;
import com.example.momogum.service.userProfileService.UserProfileServiceImpl;
import com.example.momogum.web.dto.user.UserDTO;
import com.example.momogum.web.dto.user.UserDTO.FullProfileDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/targets")
@RequiredArgsConstructor
@Tag(name = "TargetProfile API",description = "타겟 프로필 API")
public class TargetProfileController {

  private final UserProfileServiceImpl userProfileServiceImpl;

  /**
   * 상대 프로필 조회 API
   */

  @Operation(summary = "상대 프로필 조회 API", description = "상대 프로필의 전체를 조회합니다.")
  @GetMapping("{userId}/profile")
  public ApiResponse<FullProfileDTO> getFullProfile(@PathVariable Long userId) {
    return ApiResponse.onSuccess(userProfileServiceImpl.getFullProfile(userId));
  }

}
