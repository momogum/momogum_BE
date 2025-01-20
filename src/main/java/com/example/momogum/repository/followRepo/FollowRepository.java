package com.example.momogum.repository.followRepo;

import com.example.momogum.domain.FollowEntity;
import com.example.momogum.domain.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<FollowEntity,Long> {
  long countByFollower(UserEntity follower);
  long countByFollowing(UserEntity following);

}
