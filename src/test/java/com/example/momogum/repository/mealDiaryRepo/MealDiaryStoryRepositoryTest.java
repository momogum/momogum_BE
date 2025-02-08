package com.example.momogum.repository.mealDiaryRepo;

import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryStory;
import com.example.momogum.domain.common.enums.FoodCategory;
import com.example.momogum.domain.common.enums.Status;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class MealDiaryStoryRepositoryTest {

    @Autowired
    MealDiaryRepository mealDiaryRepository;

    @Autowired
    MealDiaryStoryRepository mealDiaryStoryRepository;


    @Test
    @DisplayName("findByMealDiaryIn()을 이용하여 MealDiary들을 통해 스토리를 조회 할 수 있다")
    public void findByMealDiaryIn_success(){
        //given
        MealDiary testMealDiary1 = MealDiary.builder()
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

        MealDiary testMealDiary2 = MealDiary.builder()
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

}