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
    private final String about;             // 유저 한줄소개
    private final String profileImage;
  }

}
