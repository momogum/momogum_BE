package com.example.momogum.repository.profileImageRepo;

import com.example.momogum.domain.ProfileImage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {

  // 특정 유저의 프로필 이미지 조회
  Optional<ProfileImage> findByUser_Id(Long userId);

  // 특정 유저의 프로필 이미지 존재 여부 확인
  boolean existsByUser_Id(Long userId);

  // 특정 유저의 프로필 이미지 삭제
  void deleteByUser_Id(Long userId);
}