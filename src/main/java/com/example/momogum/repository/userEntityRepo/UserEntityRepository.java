package com.example.momogum.repository.userEntityRepo;

import com.example.momogum.domain.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity,Long> {
    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.profileImage WHERE u.id = :userId")
    Optional<UserEntity> findByIdWithProfileImage(@Param("userId") Long userId);

}
