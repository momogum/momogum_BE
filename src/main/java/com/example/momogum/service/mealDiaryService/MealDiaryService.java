package com.example.momogum.service.mealDiaryService;

import com.example.momogum.web.dto.MealDairiesDTO;

public interface MealDiaryService {
    MealDairiesDTO.CreateStoryResponseDTO save(MealDairiesDTO.CreateStoryRequestDTO request);
}
