package com.example.momogum.service.userProfileService;


import com.example.momogum.web.dto.user.ProfileImageDTO;
import java.io.FileNotFoundException;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileImageService {


  List<String> uploadImages(List<MultipartFile> files, String dirName, Long userId);

  @Transactional
  void deleteImage(Long userId) throws FileNotFoundException;

  String findImagesByMealId(Long userId);
}
