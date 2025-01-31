package com.example.momogum.domain;

import com.example.momogum.domain.common.enums.ReportReason;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class MealDiaryReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ReportReason reportReason;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_entity")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_diary")
    private MealDiary mealDiary;
}
