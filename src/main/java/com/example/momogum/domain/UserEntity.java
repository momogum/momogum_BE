package com.example.momogum.domain;

import com.example.momogum.domain.common.BaseEntity;
import com.example.momogum.domain.common.enums.LoginType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    // 전화번호
    private String phoneNumber;

    // 회원 이름
    private String name;

    // 인 앱에서 사용되는 별명
    private String nickname;

    // 프로필 이미지 저장 경로
    @URL
    private String profileImage;

    // 한줄소개
    @Lob
    private String about;


    // 소셜 로그인 제공자 정보
    @Enumerated(EnumType.STRING)
    private LoginType provider;

    @Column(nullable = false, unique = true)
    private String providerId; // SNS에서 발급한 고유 ID
}
