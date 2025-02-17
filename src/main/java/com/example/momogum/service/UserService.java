package com.example.momogum.service;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.GeneralException;
import com.example.momogum.domain.utils.JwtUtil;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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

    /**
     * 요청에서 userId 가져오기 (Redis → 없으면 JWT 검증)
     */
    public Long getUserIdFromRequest(HttpServletRequest request) {
        // 1️⃣ JWT 토큰을 HTTP 헤더에서 가져오기
        String token = jwtUtil.resolveToken(request);
        if (token == null) {
            throw new GeneralException(ErrorStatus._UNAUTHORIZED);
        }

        // 2️⃣ Redis에서 userId 가져오기 (성능 최적화)
        String redisUserId = (String) redisTemplate.opsForValue().get(token);
        if (redisUserId != null) {
            return Long.parseLong(redisUserId);
        }

        // 3️⃣ Redis에 없으면, JWT에서 userId 추출
        return jwtUtil.getUserIdFromToken(token);
    }
}
