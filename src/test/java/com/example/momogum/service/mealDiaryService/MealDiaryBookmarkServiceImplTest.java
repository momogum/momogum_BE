package com.example.momogum.service.mealDiaryService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.handler.MealDiaryHandler;
import com.example.momogum.apiPayLoad.exception.handler.UserEntityHandler;
import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryBookmark;
import com.example.momogum.domain.MealDiaryImage;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.common.enums.FoodCategory;
import com.example.momogum.domain.common.enums.IsRevisit;
import com.example.momogum.repository.mealDiaryBookmarkRepo.MealDiaryBookmarkRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.mealDiary.MealDairiesDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MealDiaryBookmarkServiceImplTest {

    @InjectMocks
    private MealDiaryBookmarkServiceImpl mealDiaryBookmarkService;

    @Mock
    private MealDiaryRepository mealDiaryRepository;

    @Mock
    private MealDiaryBookmarkRepository mealDiaryBookmarkRepository;

    @Mock
    private UserEntityRepository userEntityRepository;

    UserEntity testMember;
    UserEntity testMember2;
    MealDiary testMealDiary;
    MealDiaryBookmark testBookmark;
    MealDiaryImage mealDiaryImage;

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

        mealDiaryImage = MealDiaryImage.builder()
                .imageLink("test_link")
                .imageName("test_name")
                .fileName("file_name")
                .mealDiary(null)
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
                .mealDiaryImages(List.of(mealDiaryImage))
                .build();

        mealDiaryImage.setMealDiary(testMealDiary);

        testBookmark = MealDiaryBookmark.builder()
                .userEntity(testMember2)
                .mealDiary(testMealDiary)
                .build();
    }


    @Test
    @DisplayName("toggle()를 이용해서 북마크를 추가할 수 있다")
    void toggle_success_add() {
        // given
        when(mealDiaryRepository.findById(anyLong())).thenReturn(Optional.of(testMealDiary));
        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(testMember2));
        when(mealDiaryBookmarkRepository.findByUserEntityAndMealDiary(any(), any()))
                .thenReturn(Optional.empty());
        when(mealDiaryBookmarkRepository.save(any())).thenReturn(testBookmark);

        // when
        mealDiaryBookmarkService.toggle(2L, 1L);

        // then
        verify(mealDiaryBookmarkRepository).save(any(MealDiaryBookmark.class));
        verify(mealDiaryBookmarkRepository, never()).delete(any(MealDiaryBookmark.class));
    }


    @Test
    @DisplayName("toggle()를 이용해서 북마크를 삭제할 수 있다")
    void toggle_success_delete() {
        // given
        when(mealDiaryRepository.findById(anyLong())).thenReturn(Optional.of(testMealDiary));
        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(testMember2));
        when(mealDiaryBookmarkRepository.findByUserEntityAndMealDiary(any(), any()))
                .thenReturn(Optional.of(testBookmark));

        // when
        mealDiaryBookmarkService.toggle(2L, 1L);

        // then
        verify(mealDiaryBookmarkRepository, never()).save(any(MealDiaryBookmark.class));
        verify(mealDiaryBookmarkRepository).delete(testBookmark);
    }


    @Test
    @DisplayName("존재하지 않는 게시글에 북마크를 시도하면 예외가 발생한다")
    void toggle_fail_mealDiaryNotFound() {
        // given
        when(mealDiaryRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> mealDiaryBookmarkService.toggle(2L, 999L))
                .isInstanceOf(MealDiaryHandler.class)
                .hasFieldOrPropertyWithValue("code", ErrorStatus.MEALDIARY_NOT_FOUND);
    }


    @Test
    @DisplayName("존재하지 않는 사용자가 북마크를 시도하면 예외가 발생한다")
    void toggle_fail_userNotFound() {
        // given
        when(mealDiaryRepository.findById(anyLong())).thenReturn(Optional.of(testMealDiary));
        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> mealDiaryBookmarkService.toggle(999L, 1L))
                .isInstanceOf(UserEntityHandler.class)
                .hasFieldOrPropertyWithValue("code", ErrorStatus.MEMBER_NOT_FOUND);
    }


    @Test
    @DisplayName("get()를 이용해서 사용자의 북마크 목록을 조회할 수 있다")
    void get_success() {
        // given
        List<MealDiaryBookmark> bookmarks = Collections.singletonList(testBookmark);
        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(testMember2));
        when(mealDiaryBookmarkRepository.findByUserEntity(any())).thenReturn(bookmarks);

        // when
        List<MealDairiesDTO.GetAllMealDiaryResponseDTO> response = mealDiaryBookmarkService.get(2L);

        // then
        assertThat(response).isNotNull();
        assertThat(response).isNotEmpty();
        verify(mealDiaryBookmarkRepository).findByUserEntity(testMember2);
    }


    @Test
    @DisplayName("존재하지 않는 사용자의 북마크 목록을 조회하면 예외가 발생한다")
    void get_fail_userNotFound() {
        // given
        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> mealDiaryBookmarkService.get(999L))
                .isInstanceOf(UserEntityHandler.class)
                .hasFieldOrPropertyWithValue("code", ErrorStatus.MEMBER_NOT_FOUND);
    }
}