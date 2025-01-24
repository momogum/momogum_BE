package com.example.momogum.repository.mealDiaryRepo;

import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryReport;
import com.example.momogum.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealDiaryReportRepository extends JpaRepository<MealDiaryReport, Long> {

    boolean existsByUserEntityAndMealDiary(UserEntity userEntity, MealDiary mealDiary);
}
