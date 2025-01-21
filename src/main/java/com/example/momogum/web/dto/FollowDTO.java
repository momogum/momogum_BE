package com.example.momogum.web.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class FollowDTO {


  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  // 팔로워,팔로잉 멤버 확인 DTO
  public static class FollowStatsDTO{
    private long followers;
    private long followings;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class FollowersDTO{
    private long followers;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class FollowingsDTO{
    private long followings;
  }
}
