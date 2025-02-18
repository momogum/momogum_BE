package com.example.momogum.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String token = resolveToken(request);
        log.info("🔍 JWT 필터 실행 - Authorization 헤더: '{}'", token);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            String userId = jwtTokenProvider.getUserIdFromToken(token);
            log.info("✅ JWT 검증 성공 - 사용자 ID: {}", userId);

            UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
            JwtAuthenticationToken authentication = new JwtAuthenticationToken(userDetails, token, userDetails.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            log.warn("❌ JWT 검증 실패 - SecurityContext 초기화");
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null) {
            log.info("📌 Authorization 헤더: '{}'", bearerToken); // 로깅 추가

            if (bearerToken.startsWith("Bearer ")) {
                return bearerToken.substring(7).trim(); // ✅ Bearer 제거 후 공백까지 정리
            } else {
                log.warn("🚨 Authorization 헤더가 'Bearer '로 시작하지 않음");
            }
        }

        return null;
    }
}
