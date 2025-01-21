package com.example.momogum.repository.mealDiaryRepo;

import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MealDiaryImageRepository extends JpaRepository<MealDiaryImage, Long> {

    List<MealDiaryImage> findByMealDiary(MealDiary mealDiary);

    @Query("select i from MealDiaryImage i where i.fileName = :fileName")
    Optional<MealDiaryImage> findByFileName(@Param("fileName")String fileName);

    void deleteByMealDiary(MealDiary mealDiary);
}
