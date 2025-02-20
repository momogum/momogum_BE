package com.example.momogum.service.mealDiaryService;

import com.example.momogum.web.dto.mealDiary.MealDairiesDTO;
import com.example.momogum.web.dto.mealDiary.MealDiaryReportDTO;
import com.example.momogum.web.dto.mealDiary.MealDiaryUpdateDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

public interface MealDiaryService {
    MealDairiesDTO.CreateStoryResponseDTO save(Long userId, MealDairiesDTO.CreateStoryRequestDTO request, List<MultipartFile> files);

    MealDairiesDTO.GetMealDiaryResponseDTO get(Long mealDiaryId, Long userId);

    List<MealDairiesDTO.GetAllMealDiaryResponseDTO> getAll(Long userId);

    void delete(Long userId, Long mealDiaryId) throws FileNotFoundException;

    MealDiaryReportDTO.MealDiaryReportResponseDTO report(Long userId, MealDiaryReportDTO.MealDiaryReportRequestDTO request);

    List<MealDiaryReportDTO.MealDiaryReportResponseDTO> getReport();

    MealDiaryUpdateDTO.MealDiaryUpdateResponseDTO update(Long userId, MealDiaryUpdateDTO.MealDiaryUpdateRequestDTO request);
}
