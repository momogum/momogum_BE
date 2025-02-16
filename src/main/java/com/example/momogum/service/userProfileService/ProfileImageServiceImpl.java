package com.example.momogum.service.userProfileService;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.handler.ImageHandler;
import com.example.momogum.domain.ProfileImage;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.repository.profileImageRepo.ProfileImageRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileImageServiceImpl implements ProfileImageService {

  private final AmazonS3 amazonS3;
  private final EntityManager entityManager;
  @Value("${cloud.aws.s3.bucket}")
  private String bucket;
  private final UserEntityRepository userEntityRepository;
  private final ProfileImageRepository profileImageRepository;

  private static final String DEFAULT_PROFILE_IMAGE_URL
      = "https://momogum-bucket.s3.ap-northeast-2.amazonaws.com/user-profile-images/%E1%84%86%E1%85%A5%E1%84%86%E1%85%A5%E1%84%80%E1%85%B3%E1%86%B7.png";

  /**
   * 유저 생성시 프로필 이미지 기본 이미지로 추가 메서드
   */

  @Transactional
  @Override
  public void createDefaultProfileImage(Long userId) {
    UserEntity user = userEntityRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("회원 못찾음 -> 수정필요"));

    ProfileImage defaultProfile = ProfileImage.builder()
        .user(user)
        .imageLink(DEFAULT_PROFILE_IMAGE_URL)
        .fileName("default-profile.png")
        .imageName("기본 프로필 이미지")
        .build();

    profileImageRepository.save(defaultProfile);
    log.info("기본 프로필 이미지 저장 완료: {}", defaultProfile.getImageLink());

    user.setProfileImage(defaultProfile);
    userEntityRepository.save(user);
    log.info("유저와 기본 프로필 이미지 연결 완료 userId: {}, profileImage: {}", user.getId(), user.getProfileImage().getImageLink());
  }

  /**
   * 프로필 이미지를 기본이미지로 업데이트하는 메서드입니다
   * 이미지 삭제 -> 기본 이미지로 업데이트
   */

  @Transactional
  public String setDefaultProfileImage(Long userId) {
    UserEntity user = findUser(userId);

    try {
      // 기존 이미지 있으면 삭제
      deleteImage(userId);
    }
    // 없어도 일단 진행
    catch (FileNotFoundException e) {
      log.warn("삭제할 기존 프로필 이미지가 없음: {}", e.getMessage());
    }
    // 기본 프로필 이미지 설정
    ProfileImage defaultProfile = ProfileImage.builder()
        .user(user)
        .imageLink(DEFAULT_PROFILE_IMAGE_URL)
        .fileName("default-profile.png")
        .imageName("기본 프로필 이미지")
        .build();

    profileImageRepository.save(defaultProfile);
    user.setProfileImage(defaultProfile);
    userEntityRepository.save(user);

    return DEFAULT_PROFILE_IMAGE_URL;
  }

  /**
   * 프로필 이미지를 본인이 업로드 한 것으로 업데이트하는 메서드입니다.
   * 이미지 삭제 -> 재업로드로 업데이트
   */

  @Transactional
  public String uploadCustomProfileImage(MultipartFile file, Long userId) {
    UserEntity user = findUser(userId);

    try {
      // 기존 이미지 있으면 삭제
      deleteImage(userId);
    }
    // 없어도 일단 진행
    catch (FileNotFoundException e) {
      log.warn("삭제할 기존 프로필 이미지가 없음: {}", e.getMessage());
    }

    // 새 이미지 업로드
    ProfileImage newProfileImage = uploadImage("user-profile-images", file, user);
    profileImageRepository.save(newProfileImage);
    user.setProfileImage(newProfileImage);
    userEntityRepository.save(user);

    return newProfileImage.getImageLink();
  }

  /**
   * 이미지를 업로드하는 메서드입니다
   * 사용하시는 옵션에 맞게 수정해서 사용해주시면 될 것 같습니다
   * */
  @Override
  public List<String> uploadImages(List<MultipartFile> files, String dirName, Long userId) {

    log.info("userId: {} ",userId);
    List<ProfileImage> images = new ArrayList<>();

    UserEntity byId = findUser(userId);

    try {
      images = files.parallelStream()
          .map(file -> uploadImage(dirName, file,byId))
          .collect(Collectors.toList());

      profileImageRepository.saveAll(images);

    } catch (Exception e) {
      throw new ImageHandler(ErrorStatus.IMAGE_UPLOAD_ERROR);
    }

    return images.stream()
        .map(ProfileImage::getImageLink)
        .collect(Collectors.toList());
  }


  /**
   * 프로필 이미지 삭제 메서드
   */
  @Transactional
  @Override
  public void deleteImage(Long userId) throws FileNotFoundException {

    UserEntity findUser = findUser(userId);
    ProfileImage profileImage = findUser.getProfileImage();

    if (findUser == null || profileImage == null) {
      throw new ImageHandler(ErrorStatus.IMAGE_NOT_FOUND);
    }

    try {
      // S3에서 이미지 삭제
      amazonS3.deleteObject(bucket, profileImage.getFileName());

      // UserEntity에서 ProfileImage 참조 해제
      findUser.setProfileImage(null);

      // ProfileImage의 UserEntity 참조 해제
      profileImage.setUser(null);

      // 즉시 반영하여 JPA가 기존 ProfileImage를 관리하지 않도록 함
      entityManager.flush();

      // 데이터베이스에서 ProfileImage 삭제
      profileImageRepository.delete(profileImage);

      // 영속성 컨텍스트에서 해당 엔티티 제거 (JPA가 관리하지 않도록)
      entityManager.clear();
    } catch (Exception e) {
      throw new ImageHandler(ErrorStatus.IMAGE_REMOVE_ERROR);
    }
  }


  /**
   * 매핑되어있는 정보를 통해 이미지를 찾는 메서드입니다
   * 사용하시는 옵션에 맞게 수정해서 사용해주시면 될 것 같습니다
   * */
  @Override
  public String findImagesByUserId(Long userId) {

    UserEntity findUser = findUser(userId);

    return findUser.getProfileImage()
        .getImageLink();
  }

  /**
   * 기본 이미지 조회
   */
  @Override
  public String viewProfileImage() {
    return "https://momogum-bucket.s3.ap-northeast-2.amazonaws.com/user-profile-images/%E1%84%86%E1%85%A5%E1%84%86%E1%85%A5%E1%84%80%E1%85%B3%E1%86%B7.png";
  }

  /**
   * 이미지를 업로드하는 메서드입니다
   * 사용하시는 옵션에 맞게 수정해서 사용해주시면 될 것 같습니다
   */
  private ProfileImage uploadImage(String dirName, MultipartFile file, UserEntity user) {

    String fileName = dirName + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(file.getSize());
    metadata.setContentType(file.getContentType());

    String originalFileName = file.getOriginalFilename();

    try {
      amazonS3.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata));
      log.info("아마존에 접속 성공");
    } catch (AmazonServiceException e) {
      log.error(e.getMessage());
      throw new ImageHandler(ErrorStatus.IMAGE_UPLOAD_ERROR);
    } catch (SdkClientException e) {
      throw new RuntimeException("S3 client error: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new RuntimeException("Image upload error: " + e.getMessage(), e);
    }

    String imagePath = amazonS3.getUrl(bucket, fileName).toString();


    return ProfileImage.builder()
        .user(user)
        .imageLink(imagePath)
        .fileName(fileName)
        .imageName(originalFileName)
        .build();
  }


  private UserEntity findUser(Long userId) {
    return userEntityRepository.findById(userId)
        .orElseThrow(()->new IllegalArgumentException("회원 못찾음 -> 수정필요"));
  }
}