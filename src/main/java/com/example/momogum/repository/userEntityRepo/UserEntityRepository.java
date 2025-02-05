package com.example.momogum.repository.userEntityRepo;

import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.common.enums.LoginType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    // provider와 providerId로 UserEntity 조회
    Optional<UserEntity> findByProviderAndProviderId(LoginType provider, String providerId);

    // ID로 UserEntity 조회
    Optional<UserEntity> findById(Long id);

    // Nickname 중복 여부 확인
    boolean existsByNickname(String nickname);

    // ID로 UserEntity 조회 및 Profile Image 조인
    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.profileImage WHERE u.id = :userId")
    Optional<UserEntity> findByIdWithProfileImage(@Param("userId") Long userId);

    // Keyword를 기준으로 검색
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
