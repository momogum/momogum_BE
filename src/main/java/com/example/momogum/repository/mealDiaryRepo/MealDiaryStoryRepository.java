package com.example.momogum.repository.mealDiaryRepo;

import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryStory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealDiaryStoryRepository extends JpaRepository<MealDiaryStory, Long> {

    List<MealDiaryStory> findByMealDiaryIn(List<MealDiary> mealDiary);
}
