package com.example.momogum.repository.mealDiaryRepo;

import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryStory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface MealDiaryStoryRepository extends JpaRepository<MealDiaryStory, Long> {

    List<MealDiaryStory> findByMealDiaryIn(List<MealDiary> mealDiary);

    @Modifying
    @Transactional
    @Query("DELETE FROM MealDiaryStory m WHERE m.createdAt <= :threeDaysAgo")
    int deleteByCreatedAtBefore(@Param("threeDaysAgo") LocalDateTime threeDaysAgo);
}
