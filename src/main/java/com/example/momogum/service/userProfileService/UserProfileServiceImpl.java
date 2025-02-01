package com.example.momogum.service.userProfileService;


import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.GeneralException;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.user.UserDTO;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

  private final UserEntityRepository userEntityRepository;
  //private final FollowRepository followRepository;

  // 유저 닉네암, 실명, 프로필 이미지 조회

  @Override
  @Transactional(readOnly = true)
  public UserDTO.UserResponseDTO getUserProfile(Long userId) {
    UserEntity user = userEntityRepository.findById(userId)
        .orElseThrow(() -> new GeneralException(ErrorStatus._BAD_REQUEST));

    return UserDTO.UserResponseDTO.builder()
        .id(user.getId())
        .nickname(user.getNickname())
        .name(user.getName())
        .profileImage(user.getProfileImage() != null
            ? user.getProfileImage().getImageLink()
            : "default-profile.jpg")
        .about(user.getAbout())
        .build();
  }

  // 팔로워 숫자 확인
  /*
  @Override
  @Transactional(readOnly = true)
  public FollowDTO.FollowStatsDTO getFollowStats(Long userId) {
    UserEntity user = userEntityRepository.findById(userId)
        .orElseThrow(() -> new GeneralException(ErrorStatus._BAD_REQUEST));

    long followerCount = followRepository.countByFollowing(user);
    long followingCount = followRepository.countByFollower(user);

    return FollowDTO.FollowStatsDTO.builder()
        .followers(followerCount)
        .followings(followingCount)
        .build();
  }*/

  // 유저 프로필 업데이트

  @Override
  @Transactional
  public UserDTO.UserEditDTO updateUserProfile(Long userId, UserDTO.UserEditDTO request) {
    UserEntity user = userEntityRepository.findById(userId)
        .orElseThrow(() -> new GeneralException(ErrorStatus._BAD_REQUEST));

    if (request.getNickname() != null && !request.getNickname().equals(user.getNickname())) {
      user.setNickname(request.getNickname());
    }
    if (request.getName() != null && !request.getName().equals(user.getName())) {
      user.setName(request.getName());
    }
    if (request.getAbout() != null) {
      if (!request.getAbout().equals(user.getAbout())) {
        user.setAbout(request.getAbout());
      }
    } else {
      user.setAbout("");
    }

    return UserDTO.UserEditDTO.builder()
        .nickname(user.getNickname())
        .name(user.getName())
        .about(user.getAbout() != null ? user.getAbout() : "")
        .build();
  }
}