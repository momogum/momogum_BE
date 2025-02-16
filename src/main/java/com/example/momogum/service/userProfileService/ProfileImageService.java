package com.example.momogum.service.userProfileService;


import com.example.momogum.domain.ProfileImage;
import com.example.momogum.web.dto.user.ProfileImageDTO;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileImageService {

  void createDefaultProfileImage(Long userId);

  String setDefaultProfileImage(Long userId);

  String uploadCustomProfileImage(MultipartFile file, Long userId);

  List<String> uploadImages(List<MultipartFile> files, String dirName, Long userId);

  @Transactional
  void deleteImage(Long userId) throws FileNotFoundException;

  String findImagesByUserId(Long userId);

  String viewProfileImage();
}
