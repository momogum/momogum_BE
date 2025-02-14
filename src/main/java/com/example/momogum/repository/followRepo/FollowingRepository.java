package com.example.momogum.repository.followRepo;

import com.example.momogum.domain.Following;
import com.example.momogum.domain.UserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowingRepository extends JpaRepository<Following, Long> {

  Optional<Following> findByUserAndFollowing(UserEntity user, UserEntity following);

  // 맞팔 여부 확인
  boolean existsByUserAndFollowing(UserEntity user, UserEntity following);

  // 특정 사용자의 모든 팔로우 관계 조회
  List<Following> findByUserId(Long userId);

  // 특정 사용자의 팔로우 수 카운트
  int countByUserId(Long userId);

  // 특정 사용자가 팔로우한 모든 사용자 가져오기
  @Query("SELECT f.following FROM Following f WHERE f.user.id = :userId")
  List<UserEntity> findFollowedUsersByUserId(@Param("userId") Long userId);

  // name 또는 nickname 기준으로 검색
  @Query("SELECT f FROM Following f WHERE f.user.id = :userId AND (LOWER(f.following.nickname) LIKE LOWER(CONCAT('%', :query, '%')) " +
      "OR LOWER(f.following.name) LIKE LOWER(CONCAT('%', :query, '%')))")
  List<Following> searchFollowingsByQuery(@Param("userId") Long userId, @Param("query") String query);
}
