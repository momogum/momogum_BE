package com.example.momogum.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_ProfileImages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileImage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 프로필 이미지와 유저와의 1:1 관계
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  // S3 이미지 URL
  @Column(nullable = false)
  private String imageLink;

  // S3에 저장된 파일 이름
  @Column(nullable = false)
  private String fileName;

  // 사용자가 업로드한 원본 파일 이름
  @Column
  private String imageName;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Column
  private LocalDateTime updatedAt;

}
