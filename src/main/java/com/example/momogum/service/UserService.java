package com.example.momogum.service;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.GeneralException;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.utils.JwtUtil;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final JwtUtil jwtUtil;
    private final RedisTemplate redisTemplate;

    /**
     * 닉네임 중복 확인 메서드
     * @param nickname 사용자 입력 닉네임
     * @return 중복 여부 (true: 중복됨, false: 사용 가능)
     */
    public boolean isNicknameDuplicate(String nickname) {
        boolean exists = userEntityRepository.existsByNickname(nickname);
        log.info("닉네임 '{}' 중복 여부: {}", nickname, exists);
        return exists;
    }

    public Long validateUserId(Long userId) {
        return userEntityRepository.findById(userId)
                .map(UserEntity::getId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NO_RESULT_FOUND));
    }

    @Transactional
    public void deleteUser(Long userId) {
        UserEntity user = userEntityRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));

        userEntityRepository.delete(user);
    }
}
