package com.example.momogum.service.viewMealDiaryService;

import com.example.momogum.web.dto.viewMealDiary.ViewMealDiaryDTO;

public interface ViewMealDiaryService {

    ViewMealDiaryDTO.ViewMealDiaryResponseListDTO getMealDiaryIsRevisitedByLikesCount(Long userId);

    ViewMealDiaryDTO.ViewMealDiaryResponseListDTO getMealDiaryByFoodCategory(Long userId, String foodCategory);


}
