package com.example.momogum.repository.mealDiaryRepo;

import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.common.enums.FoodCategory;
import com.example.momogum.domain.common.enums.LoginType;
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
class MealDiaryRepositoryTest {

    @Autowired
    MealDiaryRepository mealDiaryRepository;

    @Autowired
    UserEntityRepository userEntityRepository;


    @Test
    @DisplayName("findByUserEntityIn()을 이용하여 회원들과 연결된 밥일기들을 조회 할 수 있다")
    public void findByUserEntityIn_success(){
        //given
        UserEntity testUser1 = UserEntity.builder()
                .phoneNumber("test")
                .name("test")
                .nickname("test")
                .about("test")
                .provider(LoginType.KAKAO)
                .providerId("test1")
                .followerCount(0)
                .followingCount(0)
                .build();

        UserEntity testUser2 = UserEntity.builder()
                .phoneNumber("test")
                .name("test2")
                .nickname("test2")
                .about("test")
                .provider(LoginType.KAKAO)
                .providerId("test2")
                .followerCount(0)
                .followingCount(0)
                .build();

        userEntityRepository.save(testUser1);
        userEntityRepository.save(testUser2);

        MealDiary testMealDiary1 = MealDiary.builder()
                .foodCategory(FoodCategory.FAST_FOOD)
                .location("test")
                .description("test1")
                .isReport(true)
                .isReport(false)
                .likesCount(0)
                .commentCount(0)
                .status(Status.INACTIVE)
                .userEntity(testUser1)
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
                .userEntity(testUser2)
                .build();

        mealDiaryRepository.save(testMealDiary1);
        mealDiaryRepository.save(testMealDiary2);

        //when
        List<MealDiary> response = mealDiaryRepository.findByUserEntityIn(List.of(testUser1, testUser2));

        //then
        assertThat(response.size()).isEqualTo(2);
        assertThat(response.get(0)).isEqualTo(testMealDiary1);
        assertThat(response.get(0).getDescription()).isEqualTo("test1");
        assertThat(response.get(1)).isEqualTo(testMealDiary2);
        assertThat(response.get(1).getDescription()).isEqualTo("test2");
    }

}