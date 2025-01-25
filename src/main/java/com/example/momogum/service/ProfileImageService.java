package com.example.momogum.service;


import com.example.momogum.web.dto.user.ProfileImageDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileImageService {

  /**
   *프로필 이미지 업로드 메서드
   */

  ProfileImageDTO.ProfileImageResponseDTO uploadProfileImage(MultipartFile file, Long userId);

  /**
   * 유저 프로필 이미지를 삭제하는 메서드
   */

  void deleteProfileImage(Long userId);

  /**
   *기본 프로필 이미지를 설정하는 메서드*
   */

  void setDefaultProfileImage(Long userId);

}
