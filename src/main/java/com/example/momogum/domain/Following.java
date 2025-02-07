package com.example.momogum.domain;

import com.example.momogum.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "following", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "following_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Following extends BaseEntity {

    // 머랭님 이 부분을 추가적으로 제가 학습해서 기존에 이야기 했던 부분과 조금 상이할 수도 있습니다.
    // 유니크 조건을 추가하여 중복적인 팔로우는 안되도록 해두었습니다. (A -> B 인 컬럼이 있다면 A -> B가 발생할 수가 없음)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 팔로우한 사람 (내가 팔로우하는 대상)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    // 내가 팔로우하는 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private UserEntity following;

}
