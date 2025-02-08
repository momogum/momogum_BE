package com.example.momogum.service.mealDiaryService;

import com.example.momogum.apiPayLoad.exception.handler.MealDiaryStoryHandler;
import com.example.momogum.domain.*;
import com.example.momogum.domain.common.enums.FoodCategory;
import com.example.momogum.domain.common.enums.IsRevisit;
import com.example.momogum.domain.common.enums.Status;
import com.example.momogum.repository.followRepo.FollowingRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryStoryRepository;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.momogum.apiPayLoad.code.status.ErrorStatus.MEALDIARY_STORY_NOT_FOUND;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MealDiaryStoryServiceImplTest {

    @InjectMocks
    MealDiaryStoryServiceImpl mealDiaryStoryService;

    @Mock
    MealDiaryStoryRepository mealDiaryStoryRepository;

    @Mock
    MealDiaryRepository mealDiaryRepository;

    @Mock
    FollowingRepository followingRepository;


    UserEntity testMember;
    UserEntity testMember2;
    Following testFollowing;

    MealDiary testMealDiary;

    MealDiaryStory testMealDiaryStory;
    MealDiaryStory testMealDiaryStory2;
    MealDiaryImage testMealDiaryImage;


    @BeforeEach
    void setUp() {
        testMember = UserEntity.builder()
                .id(1L)
                .phoneNumber("test")
                .name("test")
                .nickname("test")
                .about("test")
                .build();

        testMember2 = UserEntity.builder()
                .id(2L)
                .phoneNumber("test2")
                .name("test2")
                .nickname("test2")
                .about("test2")
                .build();

        testFollowing = Following.builder()
                .user(testMember2)
                .following(testMember)
                .build();

        testMealDiary = MealDiary.builder()
                .id(1L)
                .foodCategory(FoodCategory.FAST_FOOD)
                .location("test")
                .description("test")
                .isReport(false)
                .isRevisit(IsRevisit.GOOD)
                .likesCount(0)
                .commentCount(0)
                .status(Status.ACTIVE)
                .inactiveDate(LocalDateTime.of(1, 1, 1, 1, 1))
                .userEntity(testMember)
                .mealDiaryImages(new ArrayList<>())
                .build();

        testMealDiaryImage = MealDiaryImage.builder()
                .id(1L)
                .imageName("test")
                .fileName("test")
                .imageLink("test")
                .mealDiary(testMealDiary)
                .build();

        testMealDiaryStory = MealDiaryStory.builder()
                .id(1L)
                .name("test")
                .mealDiary(testMealDiary)
                .build();

        testMealDiaryStory2 = MealDiaryStory.builder()
                .id(2L)
                .name("test2")
                .mealDiary(testMealDiary)
                .build();
    }

    @Test
    @DisplayName("get() 을 이용하여 개별 스토리를 조회할 수 있다")
    public void get_success() {
        // given
        MealDiary spyMealDiary = spy(testMealDiary);
        MealDiaryImage spyMealDiaryImage = spy(testMealDiaryImage);
        MealDiaryStory spyMealDiaryStory = spy(testMealDiaryStory);

        when(mealDiaryStoryRepository.findById(any())).thenReturn(Optional.of(spyMealDiaryStory));

        doReturn(spyMealDiary).when(spyMealDiaryStory).getMealDiary();
        doReturn(List.of(spyMealDiaryImage)).when(spyMealDiary).getMealDiaryImages();

        // when
        MealDiaryStoryReadDTO.MealDiaryStoryReadResponseDTO response = mealDiaryStoryService.get(spyMealDiaryStory.getId());

        // then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("test");
        assertThat(response.getMealDiaryImageLinks()).isNotEmpty();
        assertThat(response.getMealDiaryImageLinks().get(0)).isEqualTo("test");
    }


    @Test
    @DisplayName("존재하지 않는 스토리를 조회하면 정해진 예외를 반환한다")
    public void get_storyNotFound(){
        //given
        when(mealDiaryStoryRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mealDiaryStoryService.get(1111L))
                .isInstanceOf(MealDiaryStoryHandler.class)
                .hasFieldOrPropertyWithValue("code", MEALDIARY_STORY_NOT_FOUND);
    }


    @Test
    @DisplayName("getAll()을 이용하여 팔로잉 하고 있는 회원들의 스토리를 조회 할 수 있다")
    public void getAll_success(){
        // given
        when(followingRepository.findFollowedUsersByUserId(any())).thenReturn(List.of(testMember));
        when(mealDiaryRepository.findByUserEntityIn(any())).thenReturn(List.of(testMealDiary));
        when(mealDiaryStoryRepository.findByMealDiaryIn(any())).thenReturn(List.of(testMealDiaryStory));

        // when
        List<MealDiaryStoryReadDTO.MealDiaryStoryReadAllResponseDTO> response = mealDiaryStoryService.getAll(2L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(1);
    }


    @Test
    @DisplayName("조회 할 수 있는 스토리가 없으면 정해진 예외를 반환한다")
    public void title(){
        // given
        when(followingRepository.findFollowedUsersByUserId(any())).thenReturn(List.of(testMember));
        when(mealDiaryRepository.findByUserEntityIn(any())).thenReturn(List.of(testMealDiary));
        when(mealDiaryStoryRepository.findByMealDiaryIn(any())).thenReturn(List.of());

        // when & then
        assertThatThrownBy(() -> mealDiaryStoryService.getAll(1L))
                .isInstanceOf(MealDiaryStoryHandler.class)
                .hasFieldOrPropertyWithValue("code", MEALDIARY_STORY_NOT_FOUND);
    }



}