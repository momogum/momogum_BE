package com.example.momogum.service.mealDiaryService;

import com.example.momogum.web.dto.mealDiary.MealDiaryCommentCreateDTO;
import com.example.momogum.web.dto.mealDiary.MealDiaryCommentDeleteDTO;
import com.example.momogum.web.dto.mealDiary.MealDiaryCommentUpdateDTO;
import org.springframework.transaction.annotation.Transactional;

public interface MealDiaryCommentService {
    MealDiaryCommentCreateDTO.MealDiaryCommentResponseDTO create(MealDiaryCommentCreateDTO.MealDiaryCommentRequestDTO request);

    MealDiaryCommentUpdateDTO.MealDiaryCommentUpdateResponseDTO update(MealDiaryCommentUpdateDTO.MealDiaryCommentUpdateRequestDTO request);

    @Transactional
    void delete(MealDiaryCommentDeleteDTO.MealDiaryCommentDeleteRequestDTO request);
}
