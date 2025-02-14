package com.example.momogum.service.mealDiaryService;

import com.example.momogum.web.dto.mealDiary.MealDiaryStoryReadDTO;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public interface MealDiaryStoryService {
    MealDiaryStoryReadDTO.MealDiaryStoryReadResponseDTO get(Long memberId,Long storyId);

    List<MealDiaryStoryReadDTO.MealDiaryStoryReadAllResponseDTO> getAll(Long userId);

    MealDiaryStoryReadDTO.MyMealDiaryStoryReadResponseDTO getMine(Long userId);

    // 매일 자정에 실행
    @Scheduled(cron = "0 0 0 * * ?")
    void delete();
}
