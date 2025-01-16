package com.example.momogum.repository.mealDiaryRepo;

import com.example.momogum.domain.MealDiary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealDiaryRepository extends JpaRepository<MealDiary,Long> {
}
