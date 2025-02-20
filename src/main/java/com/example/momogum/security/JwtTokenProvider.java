package com.example.momogum.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    private Key key;
    private final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 30; // 30분
    private final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7; // 7일

    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // ✅ Access Token 생성 (30분 유효)
    public String createAccessToken(String userId) {
        return createToken(userId, ACCESS_TOKEN_EXPIRATION);
    }

    // ✅ Refresh Token 생성 (7일 유효)
    public String createRefreshToken(String userId) {
        return createToken(userId, REFRESH_TOKEN_EXPIRATION);
    }

    // ✅ Access / Refresh Token 생성 로직 통합
    private String createToken(String userId, long expirationTime) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ JWT에서 User ID 추출
    public String getUserIdFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // ✅ JWT 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("❌ JWT 만료: {}", e.getMessage());
        } catch (JwtException e) {
            log.warn("❌ JWT 검증 실패: {}", e.getMessage());
        }
        return false;
    }
}
