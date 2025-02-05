package com.example.momogum.repository.followRepo;

import com.example.momogum.domain.Follower;
import com.example.momogum.domain.UserEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowerRepository extends JpaRepository<Follower, Long> {
  List<Follower> findByUserId(Long userId);
  int countByUserId(Long userId);
}
