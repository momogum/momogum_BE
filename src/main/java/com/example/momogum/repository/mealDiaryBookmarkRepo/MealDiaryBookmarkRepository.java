package com.example.momogum.repository.mealDiaryBookmarkRepo;

import com.example.momogum.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MealDiaryBookmarkRepository extends JpaRepository<MealDiaryBookmark,Long> {

    Optional<MealDiaryBookmark> findByUserEntityAndMealDiary(UserEntity findUser, MealDiary findMealDiary);
}
