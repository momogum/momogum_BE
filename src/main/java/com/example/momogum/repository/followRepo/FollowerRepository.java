package com.example.momogum.repository.followRepo;

import com.example.momogum.domain.Follower;
import com.example.momogum.domain.UserEntity;
import java.util.List;

import com.example.momogum.web.dto.search.FollowStatusDTO;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowerRepository extends JpaRepository<Follower, Long> {

  List<Follower> findByUserId(Long userId);

  boolean existsByUserAndFollower(UserEntity user, UserEntity follower);

  @Query("SELECT COUNT(f) FROM Follower f WHERE f.user.id = :userId")
  int countByUserId(@Param("userId") Long userId);

  //유저와 팔로우 찾음
  Optional<Follower> findByUserAndFollower(UserEntity user, UserEntity follower);

  // name or nickname 기준으로 검색
  @Query("SELECT f FROM Follower f WHERE f.user.id = :userId AND (LOWER(f.follower.nickname) LIKE LOWER(CONCAT('%', :query, '%')) " +
         "OR LOWER(f.follower.name) LIKE LOWER(CONCAT('%', :query, '%')))")
  List<Follower> searchFollowersByQuery(Long userId, String query);


  @Query("SELECT DISTINCT new com.example.momogum.web.dto.search.FollowStatusDTO(f.user.id, f.follower.name) " +
          "FROM Follower f " +
          "WHERE f.user.id IN :searchResultUserIds " +
          "AND f.follower.id IN :myFollowingUserIds")
  List<FollowStatusDTO> findCommonFollowers(
          @Param("searchResultUserIds") List<Long> searchResultUserIds,
          @Param("myFollowingUserIds") List<Long> myFollowingUserIds
  );

  @Query("SELECT f.user.id FROM Follower f WHERE f.follower.id = :currentUserId")
  List<Long> findFollowingIdsByUserId(@Param("currentUserId") Long currentUserId);
}
