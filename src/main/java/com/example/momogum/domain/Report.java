package com.example.momogum.domain;

import com.example.momogum.domain.common.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"reporter_id", "reported_user_id"})
})
@AllArgsConstructor
@Getter
@Builder
public class Report extends BaseEntity {

    /* report도 팔로우와 비슷하게 설계를 했습니다
    다만 고려해야 하는 부분 중 하나는 중복 신고에 대해서 허용이 가능하면 (ex: a가 b를 신고 후 또 a가 b를 신고)
    각 신고를 모두 저장해야하는 경우네는 위에 unique 조건이 사라져야 합니다.
    이 부분에 대해서는 기술 정의서가 명확해지면 수정해주시면 될 듯 합니다.
    */


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 신고를 한 사람 Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private UserEntity reporterUser;

    // 신고를 받은 사람 Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_user_id" , nullable = false)
    private UserEntity reportedUser;


    @PrePersist
    @PreUpdate
    private void validateReport() {
        if (reporterUser.equals(reportedUser)) {
            throw new IllegalStateException("자기 자신을 신고할 수 없습니다.");
        }
    }
}
