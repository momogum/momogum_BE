package com.example.momogum.repository.followRepo;

import com.example.momogum.domain.Following;
import com.example.momogum.domain.UserEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowingRepository extends JpaRepository<Following,Long> {
  //long countByFollower(UserEntity follower);
  //long countByFollowing(UserEntity following);

  List<Following> findByUserId (Long userId);
  int countByUserId(Long userId);

  //특정 사용자가 팔로우한 모든 사용자 가져오는 메서드
  @Query("SELECT f.following FROM Following f WHERE f.user.id = :userId")
  List<UserEntity> findFollowedUsersByUserId(@Param("userId") Long userId);


}
