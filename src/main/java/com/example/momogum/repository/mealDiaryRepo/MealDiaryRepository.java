package com.example.momogum.repository.mealDiaryRepo;

import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.common.enums.FoodCategory;
import com.example.momogum.domain.common.enums.IsRevisit;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface MealDiaryRepository extends JpaRepository<MealDiary,Long> {

    List<MealDiary> findByUserEntity(UserEntity userEntity);

    List<MealDiary> findByUserEntityIn(List<UserEntity> userEntity);

    @Query("SELECT m.id FROM MealDiary m WHERE m.isRevisit = :isRevisit AND m.userEntity.id!= :userId and m.id not in :viewedIds ORDER BY m.likesCount DESC")
    List<Long> findAllByIsRevisit(@Param("isRevisit") IsRevisit isRevisit,
                                  @Param("userId")Long userId,
                                  @Param("viewedIds") List<Long> viewedIds,
                                  Pageable pageable);

    @Query("SELECT m.id FROM MealDiary m WHERE m.foodCategory = :foodCategory AND m.userEntity.id != :userId and  m.id not in :viewedIds ORDER BY m.likesCount DESC")
    List<Long> findAllByFoodCategory(@Param("foodCategory") FoodCategory foodCategory,
                                     @Param("userId")Long userId,
                                     @Param("viewedIds") List<Long> viewedIds,
                                     Pageable pageable);


    List<MealDiary> findByIdIn(List<Long> ids);


    @Query("SELECT DISTINCT md FROM MealDiary md " +
            "JOIN md.mealDiaryKeywords mk " +
            "JOIN mk.keyword k " +
            "WHERE LOWER(k.keyword) = LOWER(:fullKeyword) " +
            "   OR LOWER(k.keyword) = LOWER(:noSpaceKeyword) " +
            "   OR LOWER(k.keyword) LIKE LOWER(:partialKeyword) " +
            "   OR k.keyword IN (:splitKeywords) " +
            "ORDER BY mk.id ASC")
    Slice<MealDiary> searchByKeyword(
            @Param("fullKeyword") String fullKeyword,
            @Param("noSpaceKeyword") String noSpaceKeyword,
            @Param("partialKeyword") String partialKeyword,
            @Param("splitKeywords") List<String> splitKeywords
    );


}
