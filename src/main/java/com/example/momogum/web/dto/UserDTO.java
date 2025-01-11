package com.example.momogum.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class UserDTO {

  @Getter
  @Builder
  public static class Response {
    private final Long id;
    private final String nickname;          // 유저 닉네임
    private final String name;              // 유저 실명
    private final String bio;
    private final String profileImage;
    private final int followersCount;
    private final int followingCount;
  }

  @Getter
  @Setter   //유저 응답 존재
  public static class Edit {
    private String nickname;
    private String name;
    private String bio;
    private String profileImage;
  }

}
