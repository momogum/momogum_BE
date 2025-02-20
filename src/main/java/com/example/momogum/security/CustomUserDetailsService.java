package com.example.momogum.security;

import com.example.momogum.domain.UserEntity;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserEntityRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        log.info("🔍 UserDetailsService - 유저 ID 조회: {}", userId);

        UserEntity user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다. ID: " + userId));

        log.info("✅ UserEntity 조회 성공: {}", user);

        return new CustomUserDetails(user);
    }
}
