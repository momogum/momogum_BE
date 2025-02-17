package com.example.momogum.domain;

import com.example.momogum.domain.common.BaseEntity;
import com.example.momogum.domain.common.enums.FoodCategory;
import com.example.momogum.domain.common.enums.IsRevisit;
import com.example.momogum.domain.common.enums.Status;
import com.example.momogum.web.dto.mealDiary.MealDiaryUpdateDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class MealDiary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FoodCategory foodCategory;

    private String location;

    private String description;

    @Enumerated(EnumType.STRING)
    private IsRevisit isRevisit;

    private boolean isReport;

    private Integer likesCount;

    private Integer commentCount;

    private Status status;

    private LocalDateTime inactiveDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private UserEntity userEntity;

    @OneToMany(mappedBy = "mealDiary",cascade = CascadeType.ALL)
    private List<MealDiaryImage> mealDiaryImages = new ArrayList<>();

    @OneToMany(mappedBy = "mealDiary",cascade = CascadeType.ALL)
    private List<MealDiaryKeyword> mealDiaryKeywords = new ArrayList<>();

    @OneToMany(mappedBy = "mealDiary",cascade = CascadeType.ALL)
    private List<MealDiaryBookmark> mealDiaryBookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "mealDiary",cascade = CascadeType.ALL)
    private List<MealDiaryComments> mealDiaryComments = new ArrayList<>();

    @OneToMany(mappedBy = "mealDiary",cascade = CascadeType.ALL)
    private List<MealDiaryLikes> mealDiaryLikes = new ArrayList<>();

    @OneToOne(mappedBy = "mealDiary",cascade = CascadeType.ALL)
    private MealDiaryStory mealDiaryStory;

    public void removeMealDiaryImage(MealDiaryImage mealDiaryImage) {
        mealDiaryImages.remove(mealDiaryImage);
    }

    public void decreaseLikeCount() {
        this.likesCount--;
    }

    public void increaseLikeCount() {
        this.likesCount++;
    }

    public void increaseCommentCount() {
        this.commentCount++;
    }

    public void setReport(){
        this.isReport = true;
    }

    public void addMealDiaryImage(MealDiaryImage mealDiaryImage) {
        this.mealDiaryImages.add(mealDiaryImage);
    }

    public Long update(MealDiaryUpdateDTO.MealDiaryUpdateRequestDTO request){
        this.foodCategory = request.getFoodCategory();
        this.location = request.getLocation();
        this.description = request.getDescription();
        this.isRevisit = request.getRevisit();

        return this.id;
    }

    public void removeMealDiaryKeywordAll() {
        this.mealDiaryKeywords.clear();
    }


}
