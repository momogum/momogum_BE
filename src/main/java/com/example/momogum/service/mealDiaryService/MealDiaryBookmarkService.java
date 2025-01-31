package com.example.momogum.service.mealDiaryService;

import com.example.momogum.web.dto.mealDiary.MealDairiesDTO;

import java.util.List;

public interface MealDiaryBookmarkService {
    void toggle(Long userId, Long mealDiaryId);

    List<MealDairiesDTO.GetAllMealDiaryResponseDTO> get(Long userId);
}
