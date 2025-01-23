package com.example.momogum.service.mealDiaryService;

import com.example.momogum.web.dto.mealDiary.MealDiaryCommentDTO;

public interface MealDiaryCommentService {
    MealDiaryCommentDTO.MealDiaryCommentResponseDTO create(MealDiaryCommentDTO.MealDiaryCommentRequestDTO request);
}
