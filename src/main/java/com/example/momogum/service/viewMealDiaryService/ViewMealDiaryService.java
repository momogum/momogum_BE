package com.example.momogum.service.viewMealDiaryService;

import com.example.momogum.domain.common.enums.FoodCategory;
import com.example.momogum.web.dto.viewMealDiary.ViewMealDiaryDTO.MainViewMealDiaryResponse;

import java.util.List;

public interface ViewMealDiaryService {

    List<MainViewMealDiaryResponse> getMealDiaryIsRevisitedByLikesCount(Long userId, Integer page);

    List<MainViewMealDiaryResponse> getMealDiaryByFoodCategory(Long userId, Integer page ,FoodCategory foodCategory);


}
