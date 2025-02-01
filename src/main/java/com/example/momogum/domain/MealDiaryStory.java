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

    @OneToMany(mappedBy = "mealDiaryStory",cascade = CascadeType.ALL)
    private List<MealDiaryImage> mealDiaryImages = new ArrayList<>();

    @OneToOne(mappedBy = "mealDiaryStory",cascade = CascadeType.ALL)
    private MealDiary mealDiary;
}
