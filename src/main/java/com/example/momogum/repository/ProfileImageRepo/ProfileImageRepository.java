package com.example.momogum.repository.profileImageRepo;

import com.example.momogum.domain.ProfileImage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
  Optional<ProfileImage> findByUserId(Long userId);
}