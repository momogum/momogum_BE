package com.example.momogum.repository.userPreferenceRepo;

import com.example.momogum.domain.UserPreferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPreferenceRepository extends JpaRepository<UserPreferenceEntity,Long> {
}
