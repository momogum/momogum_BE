package com.example.momogum.repository.userEntityRepo;

import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.UserEntity;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity,Long> {
    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.profileImage WHERE u.id = :userId")
    Optional<UserEntity> findByIdWithProfileImage(@Param("userId") Long userId);


    @Query("SELECT DISTINCT u FROM UserEntity u " +
            "WHERE " +
            "LOWER(u.name) = LOWER(:fullKeyword) " +
            "   OR LOWER(u.nickname) = LOWER(:fullKeyword) " +
            "   OR LOWER(u.name) = LOWER(:noSpaceKeyword) " +
            "   OR LOWER(u.nickname) = LOWER(:noSpaceKeyword) " +
            "   OR LOWER(u.name) LIKE LOWER(:partialKeyword) " +
            "   OR LOWER(u.nickname) LIKE LOWER(:partialKeyword) " +
            "ORDER BY CASE " +
            "WHEN LOWER(u.name) = LOWER(:fullKeyword) OR LOWER(u.nickname) = LOWER(:fullKeyword) THEN 1 " +
            "WHEN LOWER(u.name) = LOWER(:noSpaceKeyword) OR LOWER(u.nickname) = LOWER(:noSpaceKeyword) THEN 2 " +
            "ELSE 3 END")
    List<UserEntity> searchByKeyword(
            @Param("fullKeyword") String fullKeyword,
            @Param("noSpaceKeyword") String noSpaceKeyword,
            @Param("partialKeyword") String partialKeyword
    );
}
