package com.example.momogum.repository.mealDiaryBookmarkRepo;

import com.example.momogum.domain.MealDiaryBookmark;
import com.example.momogum.domain.MealDiaryComments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealDiaryBookmarkRepository extends JpaRepository<MealDiaryBookmark,Long> {
}
