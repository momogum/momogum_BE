package com.example.momogum.service;


import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.GeneralException;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.repository.followRepo.FollowRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.FollowDTO;
import com.example.momogum.web.dto.FollowDTO.FollowStatsDTO;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserProfileService {

  private final UserEntityRepository userEntityRepository;
  private final FollowRepository followRepository;

  /**
   * 유저 정보 ID로 조회
   **/

  public Optional<UserEntity> getUserById(Long userId) {
    return userEntityRepository.findById(userId);
  }

  /**
   * 팔로워/팔로잉 수 조회
   * 0. 팔로워/팔로잉 수 카운트
   */

  public FollowDTO.FollowStatsDTO getFollowStats(Long userId) {
    UserEntity user = userEntityRepository.findById(userId)
        .orElseThrow(() -> new GeneralException(ErrorStatus._BAD_REQUEST));

    long followerCount = followRepository.countByFollowing(user);
    long followingCount = followRepository.countByFollower(user);

    return FollowDTO.FollowStatsDTO.builder()
        .followers(followerCount)
        .followings(followingCount)
        .build();
  }


}