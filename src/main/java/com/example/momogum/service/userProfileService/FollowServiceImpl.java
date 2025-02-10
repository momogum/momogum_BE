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
import java.util.List;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
  public List<FollowDTO.FollowingResponseDTO> getFollowings(Long userId) {
    UserEntity user = findUserById(userId);

    return followingRepository.findByUserId(userId).stream()
            .map(following -> FollowConverter.toFollowingResponseDTO(
                    following.getFollowing(),
                    isMutualFollow(user, following.getFollowing())
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
                    follower.getFollower(),
                    isMutualFollow(user, follower.getFollower())
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
                    following.getFollowing(),
                    isMutualFollow(user, following.getFollowing())
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
                    follower.getFollower(),
                    isMutualFollow(user, follower.getFollower())
            ))
            .collect(Collectors.toList());
  }

}
