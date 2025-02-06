package com.example.momogum.service.userProfileService;


import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.GeneralException;
import com.example.momogum.converter.userConverter.UserProfileConverter;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.validation.validator.UserProfileValidator;
import com.example.momogum.web.dto.user.UserDTO;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

  private final UserEntityRepository userEntityRepository;
  private final UserProfileValidator userProfileValidator;
  //private final FollowRepository followRepository;

  // 유저 닉네임, 실명, 프로필 이미지 조회

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

  // 유저 프로필 업데이트

  @Override
  @Transactional
  public UserDTO.UserEditDTO updateUserProfile(Long userId, UserDTO.UserEditDTO request) {
    UserEntity user = userEntityRepository.findById(userId)
        .orElseThrow(() -> new GeneralException(ErrorStatus._BAD_REQUEST));

    // 닉네임 검증 및 중복 검사
    if (request.getNickname() != null && !request.getNickname().equals(user.getNickname())) {
      userProfileValidator.validateNickname(request.getNickname());
      user.setNickname(request.getNickname());
    }
    // 유저 이름 검증
    if (request.getName() != null && !request.getName().equals(user.getName())) {
      userProfileValidator.validateName(request.getName());
      user.setName(request.getName());
    }
    // 한줄 소개 검증 (빈 문자열 반환 가능)
    user.setAbout(userProfileValidator.validateAbout(request.getAbout()));


    return UserDTO.UserEditDTO.builder()
        .nickname(user.getNickname())
        .name(user.getName())
        .about(user.getAbout() != null ? user.getAbout() : "")
        .build();
  }

  // 상대방 유저 프로필 조회

  @Override
  @Transactional
  public UserDTO.FullProfileDTO getFullProfile(Long userId) {
    UserEntity user = userEntityRepository.findById(userId)
        .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

    return UserProfileConverter.toFullProfileDTO(user);  //
  }
}