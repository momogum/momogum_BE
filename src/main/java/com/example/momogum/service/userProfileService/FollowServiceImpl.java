package com.example.momogum.service.userProfileService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.GeneralException;
import com.example.momogum.apiPayLoad.exception.handler.UserEntityHandler;
import com.example.momogum.converter.followConverter.FollowConverter;
import com.example.momogum.domain.Follower;
import com.example.momogum.domain.Following;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.repository.followRepo.FollowerRepository;
import com.example.momogum.repository.followRepo.FollowingRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.util.FirebaseCloudMessageUtil;
import com.example.momogum.web.dto.FollowDTO;
import com.example.momogum.web.dto.FollowDTO.FollowerResponseDTO;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

  private final FollowingRepository followingRepository;
  private final FollowerRepository followerRepository;
  private final UserEntityRepository userEntityRepository;
  private final FirebaseCloudMessageUtil firebaseCloudMessageUtil;

  /**
   * 현재 사용자가 특정 사용자를 팔로우하고 있는지 여부 반환
   */
  public boolean isFollowing(Long currentUserId, Long targetUserId) {
    UserEntity currentUser = userEntityRepository.findById(currentUserId)
        .orElseThrow(() -> new UserEntityHandler(ErrorStatus.MEMBER_NOT_FOUND));

    UserEntity targetUser = userEntityRepository.findById(targetUserId)
        .orElseThrow(() -> new UserEntityHandler(ErrorStatus.MEMBER_NOT_FOUND));

    return followingRepository.existsByUserAndFollowing(currentUser, targetUser);
  }

  /**
   * 팔로잉, 팔로워 수 카운트
   */

  @Override
  public FollowDTO.FollowStatsDTO getFollowStats(Long userId) {
    UserEntity user = findUserById(userId);

    return FollowDTO.FollowStatsDTO.builder()
        .followerCount(user.getFollowerCount())
        .followingCount(user.getFollowingCount())
        .build();
  }

  /**
   * 유저 조회 (userId 기반)
   */
  private UserEntity findUserById(Long userId) {
    return userEntityRepository.findById(userId).orElseThrow(
        () -> new UserEntityHandler(ErrorStatus.MEMBER_NOT_FOUND)
    );
  }

  /**
   * 팔로워 관계 삭제 메서드
   */
  private void deleteFollowRelationship(UserEntity user, UserEntity target) {
    if (followingRepository.existsByUserAndFollowing(user, target)) {
      followingRepository.deleteByUserAndFollowing(user, target);
      user.minusFollowingCount();
    }

    if (followerRepository.existsByUserAndFollower(target, user)) {
      followerRepository.deleteByUserAndFollower(target, user);
      target.minusFollowerCount();
    }
  }

  /**
   * 팔로우(언팔로우) 토글 구현
   */

  @Override
  @Transactional
  public FollowDTO.FollowStatsDTO toggleFollowUser(Long currentUserId, Long targetUserId) throws IOException {
    UserEntity follower = findUserById(currentUserId);
    UserEntity target = findUserById(targetUserId);

    boolean isFollowing = followingRepository.existsByUserAndFollowing(follower, target);

    if (isFollowing) {
      // 언팔로우
      followingRepository.deleteByUserAndFollowing(follower, target);
      followerRepository.deleteByUserAndFollower(target, follower);

      // 카운트 감소
      follower.minusFollowingCount();
      target.minusFollowerCount();
    } else {
      // 팔로우
      if (!followingRepository.existsByUserAndFollowing(follower, target)) {
        followingRepository.save(Following.builder().user(follower).following(target).build());
        follower.addFollowingCount();
      }

      if (!followerRepository.existsByUserAndFollower(target, follower)) {
        followerRepository.save(Follower.builder().user(target).follower(follower).build());
        target.addFollowerCount();
      }
    }

    String title = target.getNickname();
    String body = follower.getName()+("(@")+follower.getNickname()+(")")+"님이 회원님을 팔로우하기 시작했습니다.";
    firebaseCloudMessageUtil.sendMessageTo(targetUserId,title,body);

    return getFollowStats(currentUserId);
  }

  /**
   * 내 팔로워에서 삭제 토글 구현
   */

  @Override
  @Transactional
  public void removeFollower(Long currentUserId, Long followerId) {
    UserEntity currentUser = findUserById(currentUserId);
    UserEntity follower = findUserById(followerId);

    if (!followerRepository.existsByUserAndFollower(currentUser, follower)) {
      throw new GeneralException(ErrorStatus.TARGET_NOT_FOUND);
    }

    // 팔로워 삭제
    followerRepository.deleteByUserAndFollower(currentUser, follower);
    followingRepository.deleteByUserAndFollowing(follower, currentUser);

    // 카운트 감소
    currentUser.minusFollowerCount();
    follower.minusFollowingCount();
  }

  /**
   * 팔로잉 목록 조회 (내가 팔로우한 사람들)
   */
  @Override
  @Transactional
  public List<FollowDTO.FollowingResponseDTO> getFollowings(Long userId) {
    UserEntity user = findUserById(userId);

    return followingRepository.findByUserId(userId).stream()
        .map(following -> FollowConverter.toFollowingResponseDTO(
            following.getFollowing()
        ))
        .collect(Collectors.toList());
  }

  /**
   * 팔로워 목록 조회 (나를 팔로우하는 사람들)
   */
  @Override
  @Transactional
  public List<FollowDTO.FollowerResponseDTO> getFollowers(Long userId) {
    UserEntity user = findUserById(userId);

    return followerRepository.findByUserId(userId).stream()
        .map(follower -> FollowConverter.toFollowerResponseDTO(
            follower.getFollower()
        ))
        .collect(Collectors.toList());
  }

}
