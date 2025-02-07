package com.example.momogum.service.mealDiaryService;

import com.example.momogum.web.dto.mealDiary.MealDiaryStoryReadDTO;

import java.util.List;

public interface MealDiaryStoryService {
    MealDiaryStoryReadDTO.MealDiaryStoryReadResponseDTO get(Long storyId);

    List<MealDiaryStoryReadDTO.MealDiaryStoryReadAllResponseDTO> getAll(Long userId);
}
