package com.example.momogum.repository.mealDiaryRepo;

import com.example.momogum.domain.MealDiaryStory;
import com.example.momogum.domain.MealDiaryStoryView;
import com.example.momogum.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealDiaryStoryViewRepository extends JpaRepository<MealDiaryStoryView,Long> {
    MealDiaryStoryView findByUserEntityAndMealDiaryStory(UserEntity userEntity, MealDiaryStory mealDiaryStory);
}
