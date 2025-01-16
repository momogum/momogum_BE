package com.example.momogum.domain;

import com.example.momogum.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "follow", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"follower_id", "following_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowEntity extends BaseEntity {

    // 머랭님 이 부분을 추가적으로 제가 학습해서 기존에 이야기 했던 부분과 조금 상이할 수도 있습니다.
    // 유니크 조건을 추가하여 중복적인 팔로우는 안되도록 해두었습니다. (A -> B 인 컬럼이 있다면 A -> B가 발생할 수가 없음)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private UserEntity follower;

    @ManyToOne
    @JoinColumn(name = "following_id", nullable = false)
    private UserEntity following;


    // 자기 자신은 팔로우 못함
    // 나중에 서비스단에서 구현을 하시게 되면 없애도 될 듯 합니다 !
    @PrePersist
    @PreUpdate
    private void validateFollow() {
        if (follower.equals(following)) {
            throw new IllegalStateException("자기 자신을 팔로우할 수 없습니다.");
        }
    }
}
