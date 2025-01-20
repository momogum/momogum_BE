package com.example.momogum.domain;

import com.example.momogum.domain.common.BaseEntity;
import com.example.momogum.domain.common.enums.LoginType;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
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

    // 노션에서 해당 정보가 안나와있어 주석처리 했습니다. 나중에 필요하시면 활성화 해서 사용해주시면 될 듯합니다.
    // private String websiteLink;

    // 소셜 로그인 제공자 정보
    @Enumerated(EnumType.STRING)
    private LoginType provider;

    //유저와 팔로워,팔로우 관계 설정하기 위해서 추가하였습니다.
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FollowEntity> followings = new ArrayList<>(); // 내가 팔로우한 사람들

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FollowEntity> followers = new ArrayList<>(); // 나를 팔로우한 사람들


}
