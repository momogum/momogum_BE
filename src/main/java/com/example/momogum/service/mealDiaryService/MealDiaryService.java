package com.example.momogum.service.mealDiaryService;

import com.example.momogum.web.dto.MealDairiesDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

public interface MealDiaryService {
    MealDairiesDTO.CreateStoryResponseDTO save(MealDairiesDTO.CreateStoryRequestDTO request, List<MultipartFile> files);

    MealDairiesDTO.GetMealDiaryResponseDTO get(Long mealDiaryId);

    void delete(Long userId, Long mealDiaryId) throws FileNotFoundException;
}
