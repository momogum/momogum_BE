package com.example.momogum.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
// Spring Security 에서 사용할  JWT 인증 객체
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final UserDetails principal;  // 사용자 정보
    private final String token; // JWT 토큰

    // 생성자 : JWT 인증을 위한 정보를 받아서 초기화
    public JwtAuthenticationToken(UserDetails principal, String token, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.token = token;
        setAuthenticated(true);  // JWT 검증이 끝나고 인증된 상태로 설정
    }

    @Override
    public Object getCredentials() {
        return token;  // JWT 토큰 반환
    }

    @Override
    public Object getPrincipal() {
        return principal;  // 사용자 정보 반환
    }
}
