package com.example.momogum.domain;

import com.example.momogum.domain.common.BaseEntity;
import com.example.momogum.domain.common.enums.LoginType;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

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
    private Long id;

    // 전화번호
    private String phoneNumber;

    // 회원 이름 (실명)
    private String name;

    // 인 앱에서 사용되는 별명 (유저아이디)
    private String nickname;

    // 프로필 이미지 저장 경로
    //@URL
    //private String profileImage;

    // S3 유저 프로필 이미지
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ProfileImage profileImage;

    // 프로필 이미지와 연관관계 설정
    public void setProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;

        // 순환 호출 방지: 프로필 이미지와 유저가 이미 연결된 경우 처리하지 않음
        if (profileImage != null && profileImage.getUser() != this) {
            profileImage.setUser(this);
        }
    }

    // 한줄소개
    @Lob
    private String about;


    // 소셜 로그인 제공자 정보
    @Enumerated(EnumType.STRING)
    private LoginType provider;


    @Column(nullable = false, unique = true)
    private String providerId; // SNS에서 발급한 고유 ID

    //유저와 팔로워,팔로우 관계 설정하기 위해서 추가하였습니다.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Following> followings = new ArrayList<>(); // 내가 팔로우한 사람들

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follower> followers = new ArrayList<>(); // 나를 팔로우한 사람들

    @Column(nullable = false)
    private int followingCount = 0;
    @Column(nullable = false)
    private int followerCount = 0;

    public void addFollowingCount(){
        this.followingCount++;
    }
    public void minusFollowingCount(){
        this.followingCount = Math.max(0, this.followingCount - 1);
    }

    public void addFollowerCount(){
        this.followerCount++;
    }
    public void minusFollowerCount(){
        this.followerCount = Math.max(0, this.followerCount - 1);
    }

    public void removeProfileImage(UserEntity user){
        user.setProfileImage(null);
    }

}
