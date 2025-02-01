package com.example.momogum.service.mealDiaryService;

import com.example.momogum.web.dto.mealDiary.MealDiaryStoryReadDTO;

public interface MealDiaryStoryService {
    MealDiaryStoryReadDTO.MealDiaryStoryReadResponseDTO get(Long storyId);
}
