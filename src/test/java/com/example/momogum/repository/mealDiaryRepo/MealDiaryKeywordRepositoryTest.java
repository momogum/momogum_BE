package com.example.momogum.repository.mealDiaryRepo;

import com.example.momogum.domain.Keyword;
import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryKeyword;
import com.example.momogum.domain.common.enums.FoodCategory;
import com.example.momogum.domain.common.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class MealDiaryKeywordRepositoryTest {

    @Autowired
    MealDiaryKeywordRepository mealDiaryKeywordRepository;

    @Autowired
    MealDiaryRepository mealDiaryRepository;

    @Autowired
            KeywordRepository keywordRepository;

    MealDiary testMealDiary1;

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

        mealDiaryRepository.save(testMealDiary1);
    }


    @Test
    @DisplayName("deleteAllByMealDiary()를 매핑된 키워드를 한 번에 삭제 할 수 있다")
    public void deleteAllByMealDiary_success(){
        //given
        Keyword testKeyword1 = Keyword.builder()
                .keyword("A")
                .build();
        Keyword testKeyword2 = Keyword.builder()
                .keyword("A")
                .build();
        Keyword testKeyword3 = Keyword.builder()
                .keyword("A")
                .build();

        keywordRepository.save(testKeyword1);
        keywordRepository.save(testKeyword2);
        keywordRepository.save(testKeyword3);

        MealDiaryKeyword testMealDiaryKeyword1 = MealDiaryKeyword.builder()
                .mealDiary(testMealDiary1)
                .keyword(testKeyword1)
                .build();
        MealDiaryKeyword testMealDiaryKeyword2 = MealDiaryKeyword.builder()
                .mealDiary(testMealDiary1)
                .keyword(testKeyword2)
                .build();
        MealDiaryKeyword testMealDiaryKeyword3 = MealDiaryKeyword.builder()
                .mealDiary(testMealDiary1)
                .keyword(testKeyword3)
                .build();

        mealDiaryKeywordRepository.save(testMealDiaryKeyword1);
        mealDiaryKeywordRepository.save(testMealDiaryKeyword2);
        mealDiaryKeywordRepository.save(testMealDiaryKeyword3);

        //when
        mealDiaryKeywordRepository.deleteAllByMealDiary(testMealDiary1);

        //then
        long count = mealDiaryKeywordRepository.countByMealDiary(testMealDiary1);
        assertEquals(0, count);

        List<MealDiaryKeyword> remainingKeywords = mealDiaryKeywordRepository.findAllByMealDiary(testMealDiary1);
        assertTrue(remainingKeywords.isEmpty());
    }

}