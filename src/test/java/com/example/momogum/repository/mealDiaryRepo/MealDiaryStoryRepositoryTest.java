package com.example.momogum.repository.mealDiaryRepo;

import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryStory;
import com.example.momogum.domain.common.enums.FoodCategory;
import com.example.momogum.domain.common.enums.Status;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.util.LocalDateTimeHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class MealDiaryStoryRepositoryTest {

    @Autowired
    MealDiaryRepository mealDiaryRepository;

    @Autowired
    MealDiaryStoryRepository mealDiaryStoryRepository;

    @Mock
    LocalDateTimeHolder localDateTimeHolder;

    MealDiary testMealDiary1;
    MealDiary testMealDiary2;

    @BeforeEach
    void setUp() {

        testMealDiary1 = MealDiary.builder()
                .foodCategory(FoodCategory.FAST_FOOD)
                .location("test")
                .description("test1")
                .isReport(true)
                .isReport(false)
                .likesCount(0)
                .commentCount(0)
                .status(Status.INACTIVE)
                .userEntity(null)
                .build();

        testMealDiary2 = MealDiary.builder()
                .foodCategory(FoodCategory.FAST_FOOD)
                .location("test")
                .description("test2")
                .isReport(true)
                .isReport(false)
                .likesCount(0)
                .commentCount(0)
                .status(Status.INACTIVE)
                .userEntity(null)
                .build();

    }


    @Test
    @DisplayName("findByMealDiaryIn()을 이용하여 MealDiary들을 통해 스토리를 조회 할 수 있다")
    public void findByMealDiaryIn_success(){
        //given
        mealDiaryRepository.save(testMealDiary1);
        mealDiaryRepository.save(testMealDiary2);

        MealDiaryStory testMealDiaryStory1 = MealDiaryStory.builder()
                .name("test1")
                .mealDiary(testMealDiary1)
                .build();

        MealDiaryStory testMealDiaryStory2 = MealDiaryStory.builder()
                .name("test2")
                .mealDiary(testMealDiary2)
                .build();

        mealDiaryStoryRepository.save(testMealDiaryStory1);
        mealDiaryStoryRepository.save(testMealDiaryStory2);


        //when
        List<MealDiaryStory> response = mealDiaryStoryRepository.findByMealDiaryIn(List.of(testMealDiary1, testMealDiary2));

        //then
        assertThat(response.size()).isEqualTo(2);
        assertThat(response.get(0).getName()).isEqualTo("test1");
        assertThat(response.get(0).getId()).isEqualTo(1L);
        assertThat(response.get(1).getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("밥일기 스토리의 생성일자를 setCreatedAt()을 통해 설정 할 수 있다")
    public void setCreatedAt_success(){
        //given
        Mockito.when(localDateTimeHolder.now()).thenReturn(LocalDateTime.of(1,1,1,1,1));

        MealDiaryStory testMealDiaryStory1 = MealDiaryStory.builder()
                .name("test1")
                .mealDiary(testMealDiary1)
                .build();
        testMealDiary1.setCreatedAt(localDateTimeHolder.now());

        mealDiaryStoryRepository.save(testMealDiaryStory1);


        //when

        //then
        assertThat(testMealDiary1.getCreatedAt()).isEqualTo(LocalDateTime.of(1,1,1,1,1));
    }

}