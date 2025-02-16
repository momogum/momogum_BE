package com.example.momogum.service.mealDiaryService;

import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryImage;
import com.example.momogum.domain.MealDiaryStory;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.common.enums.FoodCategory;
import com.example.momogum.domain.common.enums.LoginType;
import com.example.momogum.repository.followRepo.FollowingRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryStoryRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryStoryViewRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.mealDiary.MealDiaryStoryReadDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MealDiaryStoryServiceImplTest {

    @InjectMocks
    MealDiaryStoryServiceImpl mealDiaryStoryService;

    @Mock
    MealDiaryRepository mealDiaryRepository;

    @Mock
    FollowingRepository followingRepository;

    @Mock
    UserEntityRepository userEntityRepository;

    @Mock
    MealDiaryStoryRepository mealDiaryStoryRepository;

    @Mock
    MealDiaryStoryViewRepository mealDiaryStoryViewRepository;

    UserEntity testMember;
    MealDiary testMealDiary;
    MealDiaryStory testMealDiaryStory;
    MealDiaryImage testMealDiaryImage;


    @BeforeEach
    void setUp() {
        testMember = UserEntity.builder()
                .id(1L)
                .phoneNumber("test_phone_number")
                .name("test_name")
                .nickname("test_nickname")
                .about("test_about")
                .provider(LoginType.KAKAO)
                .providerId("test_provider_id")
                .followerCount(0)
                .followingCount(0)
                .build();

        testMealDiaryImage = MealDiaryImage.builder()
                .id(1L)
                .imageLink("test_image_link")
                .build();

        testMealDiary = MealDiary.builder()
                .id(1L)
                .foodCategory(FoodCategory.FAST_FOOD)
                .location("test_location")
                .userEntity(testMember)
                .mealDiaryImages(List.of(testMealDiaryImage))
                .build();

        // MealDiary에 이미지 설정
        testMealDiaryImage.setMealDiary(testMealDiary);

        testMealDiaryStory = MealDiaryStory.builder()
                .id(1L)
                .name(testMember.getName())
                .mealDiary(testMealDiary)
                .build();
    }


    @Test
    @DisplayName("get()을 이용하여 스토리를 상세 조회 할 수 있다")
    public void get_success() {
        // given
        spy(testMealDiaryStory);
        spy(testMealDiary);
        spy(testMember);
        spy(testMealDiaryImage);

        when(userEntityRepository.findById(any())).thenReturn(Optional.ofNullable(testMember));
        when(mealDiaryStoryRepository.findById(any())).thenReturn(Optional.ofNullable(testMealDiaryStory));

        // when
        MealDiaryStoryReadDTO.MealDiaryStoryReadResponseDTO response = mealDiaryStoryService.get(1L, 1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(testMember.getName());
        assertThat(response.getMealDiaryImageLinks()).isEqualTo(List.of(testMealDiaryImage.getImageLink()));
        assertThat(response.getProfileImageLink()).isEqualTo(null);
        assertThat(response.getLocation()).isEqualTo(testMealDiary.getLocation());
        assertThat(response.getDescription()).isEqualTo(testMealDiary.getDescription());
    }
}