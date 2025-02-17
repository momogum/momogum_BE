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

  /**
   * 팔로우(언팔로우) 토글 구현
   */

  @Override
  @Transactional
  public FollowDTO.FollowStatsDTO toggleFollowUser(Long currentUserId, Long targetUserId) {
    UserEntity follower = findUserById(currentUserId);
    UserEntity target = findUserById(targetUserId);

    boolean isFollowing = followingRepository.existsByUserAndFollowing(follower, target);

    if (isFollowing) {
      Following following = followingRepository.findByUserAndFollowing(follower, target)
          .orElseThrow(() -> new GeneralException(ErrorStatus.TARGET_NOT_FOUND));
      // 팔로잉 삭제
      followingRepository.delete(following);

      Follower followerEntity = followerRepository.findByUserAndFollower(target, follower)
          .orElseThrow(() -> new GeneralException(ErrorStatus.TARGET_NOT_FOUND));
      // 팔로우 삭제
      followerRepository.delete(followerEntity);
    } else {
      Following newFollowing = Following.builder()
          .user(follower)
          .following(target)
          .build();
      // 팔로잉 시작
      followingRepository.save(newFollowing);

      Follower newFollower = Follower.builder()
          .user(target)
          .follower(follower)
          .build();
      // 유저에 팔로우 저장
      followerRepository.save(newFollower);
    }

    return getFollowStats(currentUserId);
  }

  /**
   * 내 팔로워에서 삭제 토글 구현
   */

  @Override
  @Transactional
  public void removeFollower(Long currentUserId, Long followerId) {
    UserEntity currentUser = userEntityRepository.findById(currentUserId)
        .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

    UserEntity follower = userEntityRepository.findById(followerId)
        .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

    // 나의 팔로워 목록에서 특정 사용자가 존재하는지 확인
    boolean isFollower = followerRepository.existsByUserAndFollower(currentUser, follower);

    if (!isFollower) {
      throw new GeneralException(ErrorStatus.TARGET_NOT_FOUND);
    }

    // 팔로워 삭제 (나의 팔로워 목록에서 제거)
    Follower followerEntity = followerRepository.findByUserAndFollower(currentUser, follower)
        .orElseThrow(() -> new GeneralException(ErrorStatus.TARGET_NOT_FOUND));
    followerRepository.delete(followerEntity);

    // 상대방의 팔로잉 목록에서 나를 제거
    Following followingEntity = followingRepository.findByUserAndFollowing(follower, currentUser)
        .orElseThrow(() -> new GeneralException(ErrorStatus.TARGET_NOT_FOUND));
    followingRepository.delete(followingEntity);

    // 팔로워/팔로잉 수 감소
    currentUser.minusFollowerCount();
    follower.minusFollowingCount();
  }


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
   *  맞팔로우 확인 메서드
   */

  public Boolean isMutualFollow(UserEntity currentUser, UserEntity targetUser) {
    return followerRepository.existsByUserAndFollower(targetUser, currentUser)
        && followerRepository.existsByUserAndFollower(currentUser, targetUser);
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

  /**
   * 유저 조회 (userId 기반)
   */
  private UserEntity findUserById(Long userId) {
    return userEntityRepository.findById(userId).orElseThrow(
        () -> new UserEntityHandler(ErrorStatus.MEMBER_NOT_FOUND)
    );
  }

  /**
   * 내가 팔로잉하는 유저 중에서 검색 (닉네임 또는 이름)
   */
  @Override
  public List<FollowDTO.FollowingResponseDTO> searchFollowingsByQuery(Long userId, String query) {
    UserEntity user = findUserById(userId);

    return followingRepository.searchFollowingsByQuery(userId, query)
        .stream()
        .map(following -> FollowConverter.toFollowingResponseDTO(
            following.getFollowing()
        ))
        .collect(Collectors.toList());
  }

  /**
   * 나를 팔로우한 유저 중에서 검색 (닉네임 또는 이름)
   */
  @Override
  public List<FollowDTO.FollowerResponseDTO> searchFollowersByQuery(Long userId, String query) {
    UserEntity user = findUserById(userId);

    return followerRepository.searchFollowersByQuery(userId, query)
        .stream()
        .map(follower -> FollowConverter.toFollowerResponseDTO(
            follower.getFollower()
        ))
        .collect(Collectors.toList());
  }

}
