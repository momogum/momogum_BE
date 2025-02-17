package com.example.momogum.repository.mealDiaryRepo;

import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealDiaryKeywordRepository extends JpaRepository<MealDiaryKeyword, Long> {

    void deleteAllByMealDiary(MealDiary mealDiary);

    long countByMealDiary(MealDiary testMealDiary);

    List<MealDiaryKeyword> findAllByMealDiary(MealDiary testMealDiary);
}
