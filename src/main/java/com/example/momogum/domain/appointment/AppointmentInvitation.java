package com.example.momogum.domain.appointment;

import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.common.BaseEntity;
import com.example.momogum.domain.common.enums.InvitationStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class AppointmentInvitation extends BaseEntity {

    // 다수 초대에 있어서 서비스단에서 각 쿼리를 만들어 보내줘야할 듯 합니다
    // 한번에 List<Long> userIds 처리는 어려울 듯 합니다

    //개별 초대 기록의 고유 식별자 (하나의 약속(appointment)에 속하는 공통 식별자가 아님)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    // 초대 상태
    @Enumerated(EnumType.STRING)
    private InvitationStatus status;

    // 초대받은 사용자 Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    // 초대된 약속 Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apm_id")
    private AppointmentName apm;

    // 초기값 pending 설정을 위해 추가
    @PrePersist
    private void prePersist() {
        if (this.status == null) {
            this.status = InvitationStatus.PENDING;
        }
    }


    public void updateStatus(InvitationStatus updateStatus) {
        this.status = updateStatus;
    }
}
