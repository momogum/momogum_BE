package com.example.momogum.repository.followRepo;

import com.example.momogum.domain.FollowEntity;
import com.example.momogum.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<FollowEntity,Long> {
}
