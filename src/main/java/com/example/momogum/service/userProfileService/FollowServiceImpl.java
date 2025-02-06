package com.example.momogum.service.userProfileService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.handler.UserEntityHandler;
import com.example.momogum.converter.followConverter.FollowConverter;
import com.example.momogum.domain.Follower;
import com.example.momogum.domain.Following;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.repository.followRepo.FollowerRepository;
import com.example.momogum.repository.followRepo.FollowingRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.FollowDTO;
import com.example.momogum.web.dto.FollowDTO.FollowerResponseDTO;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {
  private final FollowingRepository followingRepository;
  private final FollowerRepository followerRepository;
  private final UserEntityRepository userEntityRepository;


//  @Override
//  public void toggleFollowUser(Long userId, Long targetId) {
//    UserEntity follower = findUserById(userId);
//    UserEntity target = findUserById(targetId);
//
//    boolean isFollowing = followingRepository.existsByUserAndFollowing(follower, target);
//
//    if (isFollowing) {
//        // 언팔로우 처리
//        followingRepository.deleteByUserAndFollowing(follower, target);
//        follower.minusFollowingCount();  //팔로잉 카운트 감소
//        target.minusFollowerCount();  //팔로워 카운트 감소
//
//        followerRepository.deleteByUserAndFollower(target, follower);
//    } else {
//        // 팔로우 처리
//        Following newFollowing = Following.builder()
//                .user(follower)
//                .following(target)
//                .build();
//        followingRepository.save(newFollowing);
//        follower.addFollowingCount();  //팔로잉 카운트 증가
//
//        Follower newFollower = Follower.builder()
//                .user(target)
//                .follower(follower)
//                .build();
//        followerRepository.save(newFollower);
//        target.addFollowerCount();  //팔로워 카운트 증가
//    }
//  }

  /**
   * 팔로잉, 팔로워 수 카운트
   */

  @Override
  public FollowDTO.FollowStatsDTO getFollowStats(Long userId) {

    int followerCount = followerRepository.countByUserId(userId);
    int followingCount = followingRepository.countByUserId(userId);

    return  FollowDTO.FollowStatsDTO.builder()
        .followerCount(followerCount)
        .followingCount(followingCount)
        .build();
  }


  /**
   * 팔로잉 목록 조회 (내가 팔로우한 사람들)
   */
  @Override
  @Transactional
  public List<FollowDTO.FollowingResponseDTO> getFollowings(Long userId) {
    UserEntity user = userEntityRepository.findById(userId)
        .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

    return followingRepository.findByUserId(userId).stream()
        .map(following -> {
          Boolean isMutualFollow = followerRepository.existsByUserAndFollower(following.getFollowing(), user) ? true : null;
          return FollowConverter.toFollowingResponseDTO(following.getFollowing(), isMutualFollow);
        })
        .collect(Collectors.toList());
  }
  /**
   * 팔로워 목록 조회 (나를 팔로우하는 사람들)
   */
  @Override
  @Transactional
  public List<FollowDTO.FollowerResponseDTO> getFollowers(Long userId) {
    UserEntity user = userEntityRepository.findById(userId)
        .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

    return followerRepository.findByUserId(userId).stream()
        .map(follower -> {
          Boolean isMutualFollow = followingRepository.existsByUserAndFollowing(user, follower.getFollower()) ? true : null;
          return FollowConverter.toFollowerResponseDTO(follower.getFollower(), isMutualFollow);
        })
        .collect(Collectors.toList());
  }

  /**
   * 유저 조회 (userId 기반)
   */
  private UserEntity findUserById(Long userId) {
    return userEntityRepository.findById(userId).orElseThrow(
        () -> new UserEntityHandler(ErrorStatus.MEMBER_NOT_FOUND)
    );
  }

}
