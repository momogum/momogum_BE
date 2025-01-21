package com.example.momogum.repository.userEntityRepo;

import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.common.enums.LoginType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByProviderAndProviderId(LoginType provider, String providerId);
    Optional<UserEntity> findById(Long Id);
}
