package com.example.momogum.domain.appointment;

import com.example.momogum.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class AppointmentCard extends BaseEntity {

    // Flyway를 활용하면 DB에 필요한 사전정보 넣을 때 도움이 될수도 있다고 생각이 듭니다 ! 참고만 해주시면 감사하겠습니다 !

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 카드 유형
    private String type;

    // 카드 설명
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "apm_id")
    private CreateAppointmentName createAppointmentName;
}
