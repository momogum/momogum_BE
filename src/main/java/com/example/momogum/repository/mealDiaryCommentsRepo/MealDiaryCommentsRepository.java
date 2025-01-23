package com.example.momogum.repository.mealDiaryCommentsRepo;

import com.example.momogum.domain.MealDiaryComments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealDiaryCommentsRepository extends JpaRepository<MealDiaryComments,Long> {

    List<MealDiaryComments> findByMealDiaryId(Long mealDiaryId);
}
