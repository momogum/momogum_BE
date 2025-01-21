package com.example.momogum.repository.mealDiaryRepo;

import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.web.dto.viewMealDiary.ViewMealDiaryDTO;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import java.util.List;

public interface MealDiaryRepository extends JpaRepository<MealDiary,Long> {

    List<MealDiary> findByUserEntity(UserEntity userEntity);
    @Query("SELECT m.id FROM MealDiary m WHERE m.isRevisit = :isRevisit ORDER BY m.likesCount DESC")
    List<Long> findAllByIsRevisit(@Param("isRevisit") String isRevisit);

    List<MealDiary> findByIdIn(List<Long> ids);

}
