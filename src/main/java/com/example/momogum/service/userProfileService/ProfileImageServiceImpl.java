package com.example.momogum.service.userProfileService;

import static org.hibernate.query.sqm.tree.SqmNode.log;

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



  /**
   * 이미지를 업로드하는 메서드입니다
   *
   * 사용하시는 옵션에 맞게 수정해서 사용해주시면 될 것 같습니다
   * */
  @Override
  public List<String> uploadImages(List<MultipartFile> files, String dirName, Long userId) {

    log.info("userId: ",userId);
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


  @Transactional
  @Override
  public void deleteImage(Long userId) throws FileNotFoundException {

    UserEntity findUser = findUser(userId);
    List<ProfileImage> findProfileImages = profileImageRepository.findByUser(findUser);

    if (findUser == null || findProfileImages.isEmpty()) {
      throw new ImageHandler(ErrorStatus.IMAGE_NOT_FOUND);
    }

    // 엔티티 매니저를 사용하여 flush() 호출
    entityManager.flush();  // 영속성 컨텍스트의 상태를 DB에 강제로 반영

    // S3에서 이미지 삭제 및 데이터베이스 레코드 삭제
    for (ProfileImage profileImage : findProfileImages) {
      try {
        // S3에서 이미지 삭제
        amazonS3.deleteObject(bucket, profileImage.getFileName());


        /**
         *
         * JPA의 영속성 컨텍스트안에 삭제해야하는 엔티티와 연관된 엔티티가 존재함
         * 그렇기 때문에 삭제 쿼리가 발생하지 않는 오류 발견
         *
         * -> 엔티티 간의 연관관계를 끊어줌
         *
         * */
        profileImage.getUser().removeProfileImage(findUser);
        profileImage.removeUser(profileImage);



        // 데이터베이스에서 이미지 레코드 삭제
        profileImageRepository.delete(profileImage);
      } catch (Exception e) {
        throw new ImageHandler(ErrorStatus.IMAGE_REMOVE_ERROR);
      }
    }
  }


  /**
   * 매핑되어있는 정보를 통해 이미지를 찾는 메서드입니다
   *
   * 사용하시는 옵션에 맞게 수정해서 사용해주시면 될 것 같습니다
   * */
  @Override
  public String findImagesByMealId(Long userId) {

    UserEntity findUser = findUser(userId);

    return findUser.getProfileImage()
        .getImageLink();
  }

  /**
   * 이미지를 업로드하는 메서드입니다
   *
   * 사용하시는 옵션에 맞게 수정해서 사용해주시면 될 것 같습니다
   * */
  private ProfileImage uploadImage(String dirName, MultipartFile file, UserEntity user) {

    String fileName = dirName + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(file.getSize());
    metadata.setContentType(file.getContentType());

    String originalFileName = file.getOriginalFilename();

    try {
      log.info("Uploading image: {}");

      amazonS3.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata));
      log.info("Image uploaded successfully: {}");
    } catch (AmazonServiceException e) {
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