package com.example.momogum.repository.redisRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;


    // 1. 사용자가 조회한 postId 저장
    public void addViewedPost(String userId, String postId) {
        String key = generateKey(userId);
        redisTemplate.opsForSet().add(key, postId);
    }

    // 2. 사용자가 조회한 postId 목록 조회
    public Set<Long> getViewedPosts(String userId) {
        String key = generateKey(userId);

        return Objects.requireNonNull(redisTemplate.opsForSet().members(key)).stream()
                .map(Object::toString)
                .map(Long::valueOf)
                .collect(Collectors.toSet());
    }

    // 3. Redis Key 생성
    private String generateKey(String userId) {
        return "user:" + userId + ":viewed";
    }

    // 4. 세션 만료 시간 설정
    public void setSessionTimeout(String userId, long timeoutMinutes) {
        String key = generateKey(userId);
        redisTemplate.expire(key, Duration.ofMinutes(timeoutMinutes));
    }
}
