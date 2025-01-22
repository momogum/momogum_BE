package com.example.momogum.service;


import org.springframework.web.multipart.MultipartFile;

public interface UserProfileImageService  {

  /**
   * 프로필 이미지 메서드
   */

  String uploadProfileImage(MultipartFile file, String dirName, Long userId);

  /**
   * 유저 프로필 이미지를 삭제하는 메서드
   */

  void deleteProfileImage(Long userId);

  /**
   * 기본 프로필 이미지를 설정하는 메서드
   */

  void setDefaultProfileImage(Long userId);

  /**
   * 단일 유저 프로필 이미지를 조회하는 메서드
   */

  String findProfileImage(Long userId);

}
