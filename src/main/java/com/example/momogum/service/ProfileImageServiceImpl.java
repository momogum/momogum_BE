package com.example.momogum.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.handler.ImageHandler;
import com.example.momogum.domain.ProfileImage;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.repository.ProfileImageRepo.ProfileImageRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.user.ProfileImageDTO;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProfileImageServiceImpl implements ProfileImageService {

  private final AmazonS3 amazonS3;
  private final EntityManager entityManager;
  private final UserEntityRepository userRepository;
  private final ProfileImageRepository profileImageRepository;


  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  private static final String DEFAULT_IMAGE_URL = "https://example.com/default-profile.jpg";
  private static final String DEFAULT_IMAGE_NAME = "Default Profile";

  /**
   *유저 조회 메서드
   */

  private UserEntity findUser(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new ImageHandler(ErrorStatus.IMAGE_NOT_FOUND));
  }


  /**
   *유저 프로필 이미지 업로드 메서드
   **/

  @Override
  @Transactional
  public ProfileImageDTO.ProfileImageResponseDTO uploadProfileImage(MultipartFile file, Long userId) {
    UserEntity user = findUser(userId);

    // 기존 프로필 이미지 삭제
    if (user.getProfileImage() != null) {
      deleteProfileImage(user);
    }

    // 새로운 프로필 이미지 업로드

    String fileName = "profile-images/"+ UUID.randomUUID() + "-" + file.getOriginalFilename();
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
    ProfileImage newProfileImage = ProfileImage.builder()
        .imageLink(imageUrl)
        .imageName(file.getOriginalFilename())
        .fileName(fileName)
        .user(user)
        .build();

    user.setProfileImage(newProfileImage);
    profileImageRepository.save(newProfileImage);

    return ProfileImageDTO.ProfileImageResponseDTO.builder()
        .imageUrl(imageUrl)
        .imageName(file.getOriginalFilename())
        .build();
  }


  /**
   *유저 프로필 이미지를 삭제 메서드*
   **/

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
   **/

  private void deleteProfileImage(UserEntity user) {
    ProfileImage profileImage = user.getProfileImage();

    try {
      // S3에서 이미지 삭제
      amazonS3.deleteObject(bucket, profileImage.getFileName());
    } catch (Exception e) {
      throw new ImageHandler(ErrorStatus.IMAGE_REMOVE_ERROR, e);
    }

    // 데이터베이스에서 이미지 레코드 삭제
    profileImageRepository.delete(profileImage);
    user.setProfileImage(null);
  }

  /**
   ** *기본 프로필 이미지 설정 메서드*
   **/

  @Override
  @Transactional
  public void setDefaultProfileImage(Long userId) {
    UserEntity user = findUser(userId);

    // 기존 프로필 이미지 삭제 (기본 이미지가 아니면)
    if (user.getProfileImage() != null &&
        DEFAULT_IMAGE_URL.equals(user.getProfileImage().getImageLink())) {
      deleteProfileImage(user);
    }

    // 기본 이미지 설정
    ProfileImage defaultImage = ProfileImage.builder()
        .imageLink(DEFAULT_IMAGE_URL)
        .imageName(DEFAULT_IMAGE_NAME)
        .user(user)
        .build();

    user.setProfileImage(defaultImage);
    profileImageRepository.save(defaultImage);
  }
}



