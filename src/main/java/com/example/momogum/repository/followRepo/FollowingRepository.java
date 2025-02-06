package com.example.momogum.repository.followRepo;

import com.example.momogum.domain.Following;
import com.example.momogum.domain.UserEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowingRepository extends JpaRepository<Following,Long> {

  // 맞팔 여부 확인
  boolean existsByUserAndFollowing(UserEntity user, UserEntity following);

  List<Following> findByUserId (Long userId);

  @Query("SELECT COUNT(f) FROM Following f WHERE f.user.id = :userId")
  int countByUserId(@Param("userId") Long userId);

}
