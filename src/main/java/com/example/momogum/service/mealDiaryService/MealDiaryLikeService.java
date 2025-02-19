package com.example.momogum.service.mealDiaryService;

import com.example.momogum.web.dto.mealDiary.MealDiaryLikeDTO;

import java.io.IOException;
import java.util.List;

public interface MealDiaryLikeService {
    void toggle(Long userId, Long mealDiaryId) throws IOException;

    List<MealDiaryLikeDTO.MealDiaryLikeResponseDTO> get(Long mealDiaryId);
}
