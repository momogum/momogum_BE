package com.example.momogum.service.mealDiaryService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.handler.MealDiaryHandler;
import com.example.momogum.apiPayLoad.exception.handler.UserEntityHandler;
import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryLikes;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.common.enums.FoodCategory;
import com.example.momogum.domain.common.enums.IsRevisit;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryLikesRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.util.FirebaseCloudMessageUtil;
import com.example.momogum.web.dto.mealDiary.MealDiaryLikeDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MealDiaryLikeServiceImplTest {

    @InjectMocks
    MealDiaryLikeServiceImpl mealDiaryLikeService;

    @Mock
    UserEntityRepository userEntityRepository;

    @Mock
    MealDiaryRepository mealDiaryRepository;

    @Mock
    MealDiaryLikesRepository mealDiaryLikesRepository;

    @Mock
    FirebaseCloudMessageUtil firebaseCloudMessageUtil;

    UserEntity testMember;
    UserEntity testMember2;
    MealDiary testMealDiary;
    MealDiaryLikes testMealDiaryLike;

    @BeforeEach
    void setUp() {
        testMember = UserEntity.builder()
                .id(1L)
                .phoneNumber("test")
                .name("test")
                .nickname("test")
                .about("test")
                .profileImage(null)
                .build();

        testMember2 = UserEntity.builder()
                .id(2L)
                .phoneNumber("test2")
                .name("test2")
                .nickname("test2")
                .about("test2")
                .profileImage(null)
                .build();

        testMealDiary = MealDiary.builder()
                .foodCategory(FoodCategory.FAST_FOOD)
                .location("test_location")
                .description("test_description")
                .isRevisit(IsRevisit.GOOD)
                .isReport(false)
                .likesCount(0)
                .commentCount(0)
                .userEntity(testMember)
                .mealDiaryImages(null)
                .build();

        testMealDiaryLike = MealDiaryLikes.builder()
                .userEntity(testMember2)
                .mealDiary(testMealDiary)
                .build();
    }

    @Test
    @DisplayName("toggle()을 통해서 좋아요를 추가 할 수 있다")
    void toggle_success_add() throws IOException {
        // given
        when(mealDiaryRepository.findById(anyLong())).thenReturn(Optional.of(testMealDiary));
        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(testMember2));
        when(mealDiaryLikesRepository.findByUserEntityAndMealDiary(any(), any())).thenReturn(Optional.empty());
        when(mealDiaryLikesRepository.save(any())).thenReturn(testMealDiaryLike);

        // when
        mealDiaryLikeService.toggle(2L, 1L);

        // then
        verify(mealDiaryLikesRepository).save(any());
        assertThat(testMealDiary.getLikesCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("toggle()을 이용해서 좋아요를 취소 할 수 있다")
    void toggle_success_remove() throws IOException {
        // given
        when(mealDiaryRepository.findById(anyLong())).thenReturn(Optional.of(testMealDiary));
        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(testMember2));
        when(mealDiaryLikesRepository.findByUserEntityAndMealDiary(any(), any())).thenReturn(Optional.of(testMealDiaryLike));
        doNothing().when(mealDiaryLikesRepository).delete(any());

        testMealDiary.setLikesCount(1);

        // when
        mealDiaryLikeService.toggle(2L, 1L);

        // then
        verify(mealDiaryLikesRepository).delete(any());
        assertThat(testMealDiary.getLikesCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("존재하지 않는 게시글에 좋아요 시도 시, 정해진 예외를 반환한다")
    void toggle_fail_mealDiaryNotFound() {
        // given
        when(mealDiaryRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> mealDiaryLikeService.toggle(2L, 999L))
                .isInstanceOf(MealDiaryHandler.class)
                .hasFieldOrPropertyWithValue("code", ErrorStatus.MEALDIARY_NOT_FOUND);
    }

    @Test
    @DisplayName("존재하지 않는 사용자가 좋아요 시도 시, 정해진 예외를 반환한다")
    void toggle_fail_userNotFound() {
        // given
        when(mealDiaryRepository.findById(anyLong())).thenReturn(Optional.of(testMealDiary));
        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> mealDiaryLikeService.toggle(999L, 1L))
                .isInstanceOf(UserEntityHandler.class)
                .hasFieldOrPropertyWithValue("code", ErrorStatus.MEMBER_NOT_FOUND);
    }

    @Test
    @DisplayName("FCM 토큰이 있는 경우 푸시 알림을 전송한다")
    void toggle_success_withPush() throws IOException {
        // given
        testMember.setFcmToken("test_token");

        when(mealDiaryRepository.findById(anyLong())).thenReturn(Optional.of(testMealDiary));
        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(testMember2));
        when(mealDiaryLikesRepository.findByUserEntityAndMealDiary(any(), any())).thenReturn(Optional.empty());
        when(mealDiaryLikesRepository.save(any())).thenReturn(testMealDiaryLike);
        doNothing().when(firebaseCloudMessageUtil).sendMessageTo(anyLong(), anyString(), anyString());

        // when
        mealDiaryLikeService.toggle(2L, 1L);

        // then
        verify(firebaseCloudMessageUtil).sendMessageTo(anyLong(), anyString(), anyString());
    }

    @Test
    @DisplayName("get()을 이용하여 좋아요 표시된 게시글을 조회 할 수 있다")
    void get_success() {
        // given
        List<MealDiaryLikes> likes = Arrays.asList(testMealDiaryLike);

        when(mealDiaryRepository.findById(anyLong())).thenReturn(Optional.of(testMealDiary));
        when(mealDiaryLikesRepository.findByMealDiary(any())).thenReturn(likes);

        // when
        List<MealDiaryLikeDTO.MealDiaryLikeResponseDTO> result = mealDiaryLikeService.get(1L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNickname()).isEqualTo(testMember2.getNickname());
    }

    @Test
    @DisplayName("존재하지 않는 게시글을 조회하면 정해진 예외를 반환한다")
    void get_fail_mealDiaryNotFound() {
        // given
        when(mealDiaryRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> mealDiaryLikeService.get(999L))
                .isInstanceOf(MealDiaryHandler.class)
                .hasFieldOrPropertyWithValue("code", ErrorStatus.MEALDIARY_NOT_FOUND);
    }
}