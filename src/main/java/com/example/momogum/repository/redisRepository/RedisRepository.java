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


    // 사용자가 조회한 postId 저장
    public void addViewedPost(String userId, String postId) {
        String key = generateKey(userId);
        redisTemplate.opsForSet().add(key, postId);
    }

    // 사용자가 조회한 postId 목록 조회
    public Set<Long> getViewedPosts(String userId) {
        try {
            // Redis 키 생성 및 조회
            String key = generateKey(userId);
            Set<Object> redisMembers = redisTemplate.opsForSet().members(key);

            // null 체크 후 빈 집합 반환
            if (redisMembers == null) {
                return Set.of();
            }

            // 타입 변환
            return redisMembers.stream()
                    .map(Object::toString)
                    .map(Long::valueOf)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            // 예외 발생 시 빈 집합 반환 및 로깅
            System.err.println("Redis 조회 중 오류 발생: " + e.getMessage());
            return Set.of();
        }
    }

    // Redis Key 생성
    private String generateKey(String userId) {
        return "user:" + userId + ":viewed";
    }

    //  세션 만료 시간 설정
    public void setSessionTimeout(String userId, long timeoutSeconds) {
        String key = generateKey(userId);
        redisTemplate.expire(key, Duration.ofSeconds(timeoutSeconds));
    }
}
