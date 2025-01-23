package com.example.momogum.repository.mealDiaryRepo;

import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.common.enums.IsRevisit;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MealDiaryRepository extends JpaRepository<MealDiary,Long> {

    List<MealDiary> findByUserEntity(UserEntity userEntity);
    @Query("SELECT m.id FROM MealDiary m WHERE m.isRevisit = :isRevisit ORDER BY m.likesCount DESC")
    List<Long> findAllByIsRevisit(@Param("isRevisit") String isRevisit);
    @Query("SELECT m.id FROM MealDiary m WHERE m.isRevisit = :isRevisit AND m.user.Id!= :userId ORDER BY m.likesCount DESC")
    List<Long> findAllByIsRevisit(@Param("isRevisit") IsRevisit isRevisit, @Param("userId")Long userId);

    @Query("SELECT m.id FROM MealDiary m WHERE m.foodCategory = :foodCategory AND m.user.Id != :userId ORDER BY m.likesCount DESC")
    List<Long> findAllByFoodCategory(@Param("foodCategory") String foodCategory, @Param("userId")Long userId);


    List<MealDiary> findByIdIn(List<Long> ids);

}
