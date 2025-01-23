package com.example.momogum.service.mealDiaryService;

import com.example.momogum.repository.mealDiaryCommentsRepo.MealDiaryCommentsRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class MealDiaryCommentServiceImpl implements MealDiaryCommentService {

    private UserEntityRepository userEntityRepository;
    private MealDiaryRepository mealDiaryRepository;
    private MealDiaryCommentsRepository mealDiaryCommentsRepository;

}
