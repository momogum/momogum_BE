package com.example.momogum.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String phoneNumber;

    private String name;

    private String nickname;

    private String profileImage;

    @Column(columnDefinition = "TEXT")
    private String about;

    private String websiteLink;

    private LocalDateTime createdAt;

    // 소셜 로그인 제공자 정보
    private String provider;
}
