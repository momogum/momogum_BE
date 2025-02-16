package com.example.momogum.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_ProfileImages")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileImage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 프로필 이미지와 유저와의 1:1 관계
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  // User와 연관관계 설정
  public void setUser(UserEntity user) {
    this.user = user;

    if (user != null && user.getProfileImage() != this) {
      user.setProfileImage(this);
    }
  }

  // S3 이미지 URL
  @Column(nullable = false)
  private String imageLink;

  // S3에 저장된 파일 이름
  @Column(nullable = false)
  private String fileName;

  // 사용자가 업로드한 원본 파일 이름
  @Column
  private String imageName;

  public void removeUser(ProfileImage profileImage){
    profileImage.setUser(null);
  }
  
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  // 유저에 기본 이미지 할당



  // 저장 전 자동으로 현재 시간 설정
  @PrePersist
  public void prePersist() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  // 업데이트 전 자동으로 변경 시간 설정
  @PreUpdate
  public void preUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
