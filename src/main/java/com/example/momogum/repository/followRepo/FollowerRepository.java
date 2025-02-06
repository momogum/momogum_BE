package com.example.momogum.repository.followRepo;

import com.example.momogum.domain.Follower;
import com.example.momogum.domain.UserEntity;
import java.util.List;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowerRepository extends JpaRepository<Follower, Long> {

  List<Follower> findByUserId(Long userId);

  boolean existsByUserAndFollower(UserEntity user, UserEntity follower);

  @Query("SELECT COUNT(f) FROM Follower f WHERE f.user.id = :userId")
  int countByUserId(@Param("userId") Long userId);

}
