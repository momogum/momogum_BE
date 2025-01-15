package com.example.momogum.repository.followRepo;

import com.example.momogum.domain.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<FollowEntity,Long> {
}
