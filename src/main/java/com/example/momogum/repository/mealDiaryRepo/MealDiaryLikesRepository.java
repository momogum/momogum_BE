package com.example.momogum.repository.mealDiaryRepo;

import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryLikes;
import com.example.momogum.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MealDiaryLikesRepository extends JpaRepository<MealDiaryLikes,Long> {
    Optional<MealDiaryLikes> findByUserEntityAndMealDiary(UserEntity findUser, MealDiary findMealDiary);
}
