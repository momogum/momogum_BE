package com.example.momogum.web.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

public class ProfileImageDTO {

  @Getter
  @Builder
  public static class ProfileImageResponseDTO {

    private String imageUrl;     // S3에 업로드된 이미지 URL
    private String imageName;    // 업로드된 원본 파일 이름

  }

  @Getter
  @Setter
  public class ProfileImageRequestDTO {

    private Long userId;         // 유저 ID
    private MultipartFile file;  // 업로드할 이미지 파일

  }

}
