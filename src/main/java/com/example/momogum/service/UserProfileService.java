package com.example.momogum.service;


import com.example.momogum.domain.UserEntity;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserProfileService {

  private final UserEntityRepository userEntityRepository;


  /**
   * 유저 정보 ID로 조회
   **/

  public Optional<UserEntity> getUserById(Long userId) {
    return userEntityRepository.findById(userId);
  }

  /**
   * 팔로워 테이블 호출
   * 0. 팔로워/팔로잉 수 카운트
   * 1. 팔로워 / 팔로잉 -> 멤버 조회
   * 2. 멤버 조회 -> 멤버 밥일기?
   *
   */

}