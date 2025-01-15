package com.example.momogum.repository.userEntityRepo;

import com.example.momogum.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntityRepository extends JpaRepository<UserEntity,Long> {
}
