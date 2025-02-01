package com.example.momogum.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class MealDiaryImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이미지가 저장된 주소 링크
    private String imageLink;

    private String fileName;

    private String imageName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_diary_id")
    private MealDiary mealDiary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_diary_story_id")
    private MealDiaryStory mealDiaryStory;


    public void setMealDiary(MealDiary mealDiary) {
        if (this.mealDiary != null){
            this.mealDiary = mealDiary;
        }
    }

    public void removeMealDiary() {
        this.mealDiary = null;
    }
}
