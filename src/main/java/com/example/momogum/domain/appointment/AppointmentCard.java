package com.example.momogum.domain.appointment;

import com.example.momogum.domain.common.BaseEntity;
import com.example.momogum.domain.common.enums.CardCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AppointmentCard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    private String imageUrl; // 선택된 카드 URL

    @Enumerated(EnumType.STRING)
    private CardCategory category;

}
