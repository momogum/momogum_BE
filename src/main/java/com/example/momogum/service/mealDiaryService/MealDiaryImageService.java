package com.example.momogum.service.mealDiaryService;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

public interface MealDiaryImageService {

    List<String> uploadImages(List<MultipartFile> files, String dirName, Long mealDiaryId);

    void deleteImage(Long mealDiaryId) throws FileNotFoundException;

    String findImageByFileName(String fileName);

    List<String> findImagesByMealId(Long mealDiaryId);
}
