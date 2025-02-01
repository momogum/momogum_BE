package com.example.momogum.domain;

import com.example.momogum.domain.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "follower", uniqueConstraints = {
    // 유저 아이디 = 팔로우를 받는 사람
    @UniqueConstraint(columnNames = {"follower_id", "user_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Follower extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 팔로우 받은 사람 (팔로우하는 대상)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  // 나를 팔로우하는 사람 (팔로워)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "follower_id", nullable = false)
  private UserEntity follower;


  // 자기 자신은 팔로우 못함
  @PrePersist
  @PreUpdate
  private void validateFollow() {
    if (follower != null && user != null && follower.equals(user)) {
      throw new IllegalStateException("자기 자신을 팔로우할 수 없습니다.");
    }

  }
}
