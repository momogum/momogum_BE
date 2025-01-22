package com.example.momogum.repository.mealDiaryRepo;

import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealDiaryRepository extends JpaRepository<MealDiary,Long> {

    List<MealDiary> findByUserEntity(UserEntity userEntity);
}
