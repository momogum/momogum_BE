package com.example.momogum.security;

import com.example.momogum.domain.UserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {
    private final UserEntity user;

    public CustomUserDetails(UserEntity user) {
        this.user = user;
    }

    public Long getId() { // 🔹 getId() 메서드 추가
        return user.getId();
    }

    @Override
    public String getUsername() {
        return user.getNickname(); // or other unique identifier
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // 권한이 필요하면 설정
    }

    @Override
    public String getPassword() {
        return null; // 패스워드 필드가 없으면 null
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
