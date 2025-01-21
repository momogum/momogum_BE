package com.example.momogum.web.dto.user;

import com.example.momogum.web.dto.UserDTO;
import com.example.momogum.web.dto.UserDTO.Response;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

public class UserEditDTO {

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Request {

    @Schema(description = "유저 네임", example = "kyum", required = true)
    @NotBlank(message = "닉네임은 필수입니다")
    @Length(min = 2, max = 20, message = "닉네임은 2글자 이상 20글자 이하로 입력해주세요.")
    private String nickname;

    @Schema(description = "유저 실명", example = "규민", required = true)
    @NotBlank(message = "이름을 입력해주세요")
    @Length(min = 2, max = 20, message = "이름은 2글자 이상 20글자 이하로 입력해주세요.")
    private String name;

    @Schema(description = "한줄 소개", example = "덕규입니다", required = false)
    @Length(min = 2, max = 20, message = "한줄 소개는 2글자 이상 20글자 이하로 입력해주세요.")
    private String about;

    @Schema(description = "유저 프로필 사진 URL", example = "http://example.com/profile.jpg", required = true)
    @URL(message = "유효한 URL 형식을 입력해주세요.")
    private String profileImage;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Response {

    @Schema(description = "유저 네임", example = "kyum", required = true)
    private String nickname;

    @Schema(description = "유저 실명", example = "규민", required = true)
    private String name;

    @Schema(description = "한줄 소개", example = "덕규입니다", required = true)
    private String about;

    @Schema(description = "유저 프로필 사진 URL", example = "http://example.com/profile.jpg", required = true)
    private String profileImage;
  }


}
