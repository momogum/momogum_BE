package com.example.momogum.domain.appointment;

import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class AppointmentName extends BaseEntity {

    // Column 길이는 따로 지정 해두지 않았습니다.
    // 기술 정의서에서 디테일하게 잡히는 부분이 생기면 추가 해주시면 감사하겠습니다.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 약속 이름
    private String name;

    // 식사 메뉴 (예: 돈까스, 파스타 etc)
    private String menu;

    // 약속 날짜 및 시간
    private LocalDateTime date;

    // 약속 장소
    private String location;

    // 추가 메모
    @Lob
    private String notes;

    // 약속 생성자 Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
}
