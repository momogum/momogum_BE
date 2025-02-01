package com.example.momogum.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class MealDiaryStory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 업로드한 회원의 이름을 필드로 잡았습니다
     * 매번 조회 로직 실행할때마다 회원을 조회하는게 아니라 회원의 name을 초반에 필드로 저장해두고
     * 밥일기 엔티티만 조회하도록 구현하겠습니다
     * */
    private String name;

    @OneToMany(mappedBy = "mealDiaryStory",cascade = CascadeType.ALL)
    private List<MealDiaryImage> mealDiaryImages = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    private MealDiary mealDiary;
}
