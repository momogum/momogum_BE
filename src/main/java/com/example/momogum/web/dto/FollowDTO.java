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

  public static class FollowingResponseDTO {
    private String username;
    private String name;
    private String profileImage;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class FollowerResponseDTO{
    private String username;
    private String name;
    private String profileImage;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class FollowStatsDTO{
    private int followerCount;
    private int followingCount;
  }
}
