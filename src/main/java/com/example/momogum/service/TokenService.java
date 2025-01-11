package com.example.momogum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RedisTemplate<String, String> redisTemplate;

    // Access Token 생성
    public String createAccessToken(String userId) {
        return "access-token-" + userId;
    }

    // Refresh Token 생성
    public String createRefreshToken(String userId) {
        return "refresh-token-" + userId;
    }

    // 토큰 저장
    public void saveTokens(String accessToken, String refreshToken, String userId) {
        redisTemplate.opsForValue().set(accessToken, userId, 30, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(refreshToken, userId, 7, TimeUnit.DAYS);
    }
}
