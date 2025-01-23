package com.example.momogum.repository.mealDiaryCommentsRepo;

import com.example.momogum.domain.MealDiaryComments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealDiaryCommentsRepository extends JpaRepository<MealDiaryComments,Long> {

    MealDiaryComments findByMealDiaryId(Long mealDiaryId);
}
