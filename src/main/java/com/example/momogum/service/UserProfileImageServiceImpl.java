package com.example.momogum.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.handler.ImageHandler;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.UserProfileImage;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserProfileImageServiceImpl implements UserProfileImageService {

  private final AmazonS3 amazonS3;
  @Value("${cloud.aws.s3.bucket}")
  private String bucket;
  private final UserEntityRepository userRepository;
  private final UserProfileImageRepository userImageRepository;

  /**
   * 유저 프로필 이미지를 업로드하는 메서드
   */
  @Override
  @Transactional
  public String uploadProfileImage(MultipartFile file, String dirName, Long userId) {
    UserEntity user = findUser(userId);

    // 기존 프로필 이미지 삭제
    if (user.getProfileImage() != null) {
      deleteProfileImage(user);
    }

    // 새로운 프로필 이미지 업로드

    String fileName = dirName + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(file.getSize());
    metadata.setContentType(file.getContentType());

    try {
      amazonS3.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata));
    } catch (AmazonServiceException e) {
      throw new ImageHandler(ErrorStatus.IMAGE_UPLOAD_ERROR);
    } catch (SdkClientException | IOException e) {
      throw new RuntimeException("Image upload error: " + e.getMessage(), e);
    }

    String imageUrl = amazonS3.getUrl(bucket, fileName).toString();

    // 새로운 프로필 이미지 엔티티 생성 및 저장
    UserProfileImage newProfileImage = UserProfileImage.builder()
        .imageLink(imageUrl)
        .fileName(fileName)
        .imageName(file.getOriginalFilename())
        .user(user)
        .build();

    user.setProfileImage(newProfileImage);
    userProfileImageRepository.save(newProfileImage);

    return imageUrl;
  }


  /**
   * 유저 프로필 이미지를 삭제하는 메서드
   */

  @Override
  @Transactional
  public void deleteProfileImage(Long userId) {
    UserEntity user = findUser(userId);

    if (user.getProfileImage() == null) {
      throw new ImageHandler(ErrorStatus.IMAGE_NOT_FOUND);
    }

    deleteProfileImage(user);
  }


  /**
   * S3와 데이터베이스에서 프로필 이미지 삭제
   */

  private void deleteProfileImage(UserEntity user) {
    UserProfileImage profileImage = user.getProfileImage();

    try {
      // S3에서 이미지 삭제
      amazonS3.deleteObject(bucket, profileImage.getFileName());

      // 데이터베이스에서 이미지 레코드 삭제
      userProfileImageRepository.delete(profileImage);
    } catch (Exception e) {
      throw new ImageHandler(ErrorStatus.IMAGE_REMOVE_ERROR);
    }

    // 데이터베이스에서 이미지 레코드 삭제
    userProfileImageRepository.delete(profileImage);
    user.setProfileImage(null);
  }

  /**
   * 기본 프로필 이미지를 설정하는 메서드
   */

  @Transactional
  public void setDefaultProfileImage(Long userId) {
    UserEntity user = findUser(userId);

    // 기존 프로필 이미지 삭제
    if (user.getProfileImage() != null) {
      deleteProfileImage(user);
    }

    // 기본 이미지 설정
    String defaultImageUrl = "https://example.com/default-profile.jpg";
    UserProfileImage defaultImage = UserProfileImage.builder()
        .fileName("default-profile.jpg")
        .imageLink(defaultImageUrl)
        .user(user)
        .build();

    user.setProfileImage(defaultImage);
    userProfileImageRepository.save(defaultImage);
  }

  /**
   * 단일 유저 프로필 이미지를 조회하는 메서드
   */

  @Override
  public String findProfileImage(Long userId) {
    UserEntity user = findUser(userId);

    if (user.getProfileImage() == null) {
      throw new ImageHandler(ErrorStatus.IMAGE_NOT_FOUND);
    }

    return user.getProfileImage().getImageLink();
  }

}



