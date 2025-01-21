package com.example.momogum.service;


import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.GeneralException;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.repository.followRepo.FollowRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.FollowDTO;
import com.example.momogum.web.dto.FollowDTO.FollowStatsDTO;
import com.example.momogum.web.dto.UserDTO;
import com.example.momogum.web.dto.user.UserEditDTO;
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

  public UserDTO.Response getUserProfile(Long userId) {
    UserEntity user = userEntityRepository.findById(userId)
        .orElseThrow(() -> new GeneralException(ErrorStatus._BAD_REQUEST));

    return UserDTO.Response.builder()
        .id(user.getId())
        .nickname(user.getNickname())
        .name(user.getName())
        .profileImage(user.getProfileImage())
        .about(user.getAbout())
        .build();
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

  /**
   * 팔로워/ 팔로잉 멤버 조회
   * 0. 멤버 조회
   * 1. 멤버 프로필
   */

  /**
   * 유저 프로필 수정
   * 0. 프로필 이미지 편집
   * 1. 기본 이미지
   * 2. 갤러리에서 사진 첨부
   * 3. 이름, 닉네임, 한줄 소개 수정
   */

  public UserEditDTO.Response updateUserProfile(Long userId, UserEditDTO.Request request) {
    // 유저 데이터 조회
    UserEntity user = userEntityRepository.findById(userId)
        .orElseThrow(() -> new GeneralException(ErrorStatus._BAD_REQUEST));

    user.setNickname(request.getNickname());
    user.setName(request.getName());
    user.setAbout(request.getAbout());
    user.setProfileImage(request.getProfileImage());

    // 수정된 데이터 저장
    userEntityRepository.save(user);

    return UserEditDTO.Response.builder()
        .nickname(user.getNickname())
        .name(user.getName())
        .about(user.getAbout())
        .profileImage(user.getProfileImage())
        .build();
  }



}