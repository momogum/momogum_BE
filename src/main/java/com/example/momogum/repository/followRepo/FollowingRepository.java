package com.example.momogum.repository.followRepo;

import com.example.momogum.domain.Following;
import com.example.momogum.domain.UserEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowingRepository extends JpaRepository<Following,Long> {
  //long countByFollower(UserEntity follower);
  //long countByFollowing(UserEntity following);

  List<Following> findByUserId (Long userId);
  int countByUserId(Long userId);
}
