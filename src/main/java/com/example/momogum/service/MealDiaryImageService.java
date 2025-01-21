package com.example.momogum.service;

import com.example.momogum.domain.MealDiary;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface MealDiaryImageService {
    String upload(MultipartFile multipartFile, String dirName, Long mealDiaryId) throws IOException;

    File convert(MultipartFile file) throws IOException;

    String putS3(File uploadFile, String fileName);

    void removeNewFile(File targetFile);

    void saveFileMetadata(String originalFileName, String uniqueFileName,
                          String uploadImageUrl, MealDiary mealDiary);

    byte[] download(Long mealDiaryId) throws IOException;

    // memberId로 이미지 조회
    byte[] downloadImage(Long mealDiaryId) throws IOException;

    @Transactional
    void deleteFile(Long mealDiaryId) throws FileNotFoundException;

    String updateProfileImage(MultipartFile newImageFile, String dirName, Long mealDiaryId) throws IOException;
}
