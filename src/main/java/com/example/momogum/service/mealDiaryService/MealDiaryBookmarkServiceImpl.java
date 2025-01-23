package com.example.momogum.service.mealDiaryService;

import com.example.momogum.repository.mealDiaryBookmarkRepo.MealDiaryBookmarkRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryLikesRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MealDiaryBookmarkServiceImpl implements MealDiaryBookmarkService {

    private final MealDiaryRepository mealDiaryRepository;
    private final MealDiaryBookmarkRepository mealDiaryBookmarkRepository;
    private final UserEntityRepository userEntityRepository;


}
