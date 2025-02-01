package com.example.momogum.service.mealDiaryService;

import com.example.momogum.repository.mealDiaryRepo.MealDiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MealDiaryStoryServiceImpl {

    private final MealDiaryRepository mealDiaryRepository;
}
