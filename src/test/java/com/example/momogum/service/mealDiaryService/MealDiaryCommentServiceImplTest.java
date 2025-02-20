package com.example.momogum.service.mealDiaryService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.handler.MealDiaryCommentHandler;
import com.example.momogum.apiPayLoad.exception.handler.MealDiaryHandler;
import com.example.momogum.apiPayLoad.exception.handler.UserEntityHandler;
import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryComments;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.common.enums.FoodCategory;
import com.example.momogum.domain.common.enums.IsRevisit;
import com.example.momogum.repository.mealDiaryCommentsRepo.MealDiaryCommentsRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.util.FirebaseCloudMessageUtil;
import com.example.momogum.web.dto.mealDiary.MealDiaryCommentCreateDTO;
import com.example.momogum.web.dto.mealDiary.MealDiaryCommentDeleteDTO;
import com.example.momogum.web.dto.mealDiary.MealDiaryCommentUpdateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.example.momogum.apiPayLoad.code.status.ErrorStatus.IMAGE_UPLOAD_ERROR;
import static com.example.momogum.apiPayLoad.code.status.ErrorStatus.MEALDIARY_NOT_FOUND;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MealDiaryCommentServiceImplTest {

    @InjectMocks
    MealDiaryCommentServiceImpl mealDiaryCommentService;

    @Mock
    UserEntityRepository userEntityRepository;

    @Mock
    MealDiaryRepository mealDiaryRepository;

    @Mock
    MealDiaryCommentsRepository mealDiaryCommentsRepository;

    @Mock
    FirebaseCloudMessageUtil firebaseCloudMessageUtil;

    UserEntity testMember;
    UserEntity testMember2;
    MealDiary testMealDiary;

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
    }


    @Test
    @DisplayName("create()를 이용해서 타인의 게시글에 댓글을 달 수 있다")
    public void create_success() throws IOException {
        // given
        MealDiaryCommentCreateDTO.MealDiaryCommentRequestDTO request = MealDiaryCommentCreateDTO.MealDiaryCommentRequestDTO.builder()
                .userId(2L)
                .mealDiaryId(1L)
                .comment("test_comment")
                .build();

        MealDiaryComments savedComment = MealDiaryComments.builder()
                .id(1L)
                .content("test_comment")
                .mealDiary(testMealDiary)
                .user(testMember2)
                .build();

        when(mealDiaryRepository.findById(anyLong())).thenReturn(Optional.of(testMealDiary));
        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(testMember2));
        when(mealDiaryCommentsRepository.save(any(MealDiaryComments.class))).thenReturn(savedComment);

        // when
        MealDiaryCommentCreateDTO.MealDiaryCommentResponseDTO response = mealDiaryCommentService.create(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getMealDiaryCommentId()).isEqualTo(1L);

        verify(mealDiaryRepository).findById(1L);
        verify(userEntityRepository).findById(2L);
        verify(mealDiaryCommentsRepository).save(any(MealDiaryComments.class));

        assertThat(testMealDiary.getCommentCount()).isEqualTo(1);
    }


    @Test
    @DisplayName("존재하지 않는 게시글에 댓글을 달면 예외가 발생한다")
    public void create_fail_mealDiaryNotFound() {
        // given
        MealDiaryCommentCreateDTO.MealDiaryCommentRequestDTO request = MealDiaryCommentCreateDTO.MealDiaryCommentRequestDTO.builder()
                .userId(2L)
                .mealDiaryId(999L)
                .comment("test_comment")
                .build();

        when(mealDiaryRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> mealDiaryCommentService.create(request))
                .isInstanceOf(MealDiaryHandler.class)
                .hasFieldOrPropertyWithValue("code", MEALDIARY_NOT_FOUND);
    }


    @Test
    @DisplayName("존재하지 않는 사용자가 댓글을 달면 예외가 발생한다")
    public void create_fail_userNotFound() {
        // given
        MealDiaryCommentCreateDTO.MealDiaryCommentRequestDTO request = MealDiaryCommentCreateDTO.MealDiaryCommentRequestDTO.builder()
                .userId(999L)
                .mealDiaryId(1L)
                .comment("test_comment")
                .build();

        when(mealDiaryRepository.findById(anyLong())).thenReturn(Optional.of(testMealDiary));
        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> mealDiaryCommentService.create(request))
                .isInstanceOf(UserEntityHandler.class)
                .hasFieldOrPropertyWithValue("code", ErrorStatus.MEMBER_NOT_FOUND);
    }


    @Test
    @DisplayName("댓글을 작성하면 게시글 작성자에게 푸시 알림을 보낼 수 있다")
    public void create_success_push() throws IOException {
        // given
        MealDiaryCommentCreateDTO.MealDiaryCommentRequestDTO request = MealDiaryCommentCreateDTO.MealDiaryCommentRequestDTO.builder()
                .userId(2L)
                .mealDiaryId(1L)
                .comment("test_comment")
                .build();

        MealDiaryComments savedComment = MealDiaryComments.builder()
                .id(1L)
                .content("test_comment")
                .mealDiary(testMealDiary)
                .user(testMember2)
                .build();

        testMember.setFcmToken("test_token");

        when(mealDiaryRepository.findById(anyLong())).thenReturn(Optional.of(testMealDiary));
        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(testMember2));
        when(mealDiaryCommentsRepository.save(any(MealDiaryComments.class))).thenReturn(savedComment);
        doNothing().when(firebaseCloudMessageUtil).sendMessageTo(anyLong(), anyString(), anyString());

        // when
        MealDiaryCommentCreateDTO.MealDiaryCommentResponseDTO response = mealDiaryCommentService.create(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getMealDiaryCommentId()).isEqualTo(1L);

        verify(firebaseCloudMessageUtil).sendMessageTo(anyLong(), anyString(), anyString());
    }


    @Test
    @DisplayName("FCM 토큰이 없으면 푸시를 전달하지 않고 댓글을 달 수 있다")
    public void create_success_noPush() throws IOException {
        // given
        MealDiaryCommentCreateDTO.MealDiaryCommentRequestDTO request = MealDiaryCommentCreateDTO.MealDiaryCommentRequestDTO.builder()
                .userId(2L)
                .mealDiaryId(1L)
                .comment("test_comment")
                .build();

        MealDiaryComments savedComment = MealDiaryComments.builder()
                .id(1L)
                .content("test_comment")
                .mealDiary(testMealDiary)
                .user(testMember2)
                .build();

        when(mealDiaryRepository.findById(anyLong())).thenReturn(Optional.of(testMealDiary));
        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(testMember2));
        when(mealDiaryCommentsRepository.save(any(MealDiaryComments.class))).thenReturn(savedComment);

        // when
        MealDiaryCommentCreateDTO.MealDiaryCommentResponseDTO response = mealDiaryCommentService.create(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getMealDiaryCommentId()).isEqualTo(1L);

        verify(firebaseCloudMessageUtil,times(0)).sendMessageTo(anyLong(), anyString(), anyString());
    }


    @Test
    @DisplayName("댓글을 작성하면 게시글의 댓글 카운트가 늘어난다")
    public void create_success_countUp() throws IOException {
        MealDiaryCommentCreateDTO.MealDiaryCommentRequestDTO request = MealDiaryCommentCreateDTO.MealDiaryCommentRequestDTO.builder()
                .userId(2L)
                .mealDiaryId(1L)
                .comment("test_comment")
                .build();

        MealDiaryComments savedComment = MealDiaryComments.builder()
                .id(1L)
                .content("test_comment")
                .mealDiary(testMealDiary)
                .user(testMember2)
                .build();

        when(mealDiaryRepository.findById(anyLong())).thenReturn(Optional.of(testMealDiary));
        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(testMember2));
        when(mealDiaryCommentsRepository.save(any(MealDiaryComments.class))).thenReturn(savedComment);

        // when
        MealDiaryCommentCreateDTO.MealDiaryCommentResponseDTO response = mealDiaryCommentService.create(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getMealDiaryCommentId()).isEqualTo(1L);
        assertThat(testMealDiary.getCommentCount()).isEqualTo(1);
    }


    @Test
    @DisplayName("delete()를 이용하여 댓글을 삭제할 수 있다")
    public void delete_success() {
        // given
        MealDiaryCommentDeleteDTO.MealDiaryCommentDeleteRequestDTO request =
                MealDiaryCommentDeleteDTO.MealDiaryCommentDeleteRequestDTO.builder()
                        .userId(2L)
                        .mealDiaryCommentId(1L)
                        .build();

        MealDiaryComments comment = MealDiaryComments.builder()
                .id(1L)
                .content("test_comment")
                .mealDiary(testMealDiary)
                .user(testMember2)
                .build();

        when(mealDiaryCommentsRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(testMember2));
        doNothing().when(mealDiaryCommentsRepository).delete(any(MealDiaryComments.class));

        // when
        mealDiaryCommentService.delete(request);

        // then
        verify(mealDiaryCommentsRepository).findById(1L);
        verify(userEntityRepository).findById(2L);
        verify(mealDiaryCommentsRepository).delete(comment);
        assertThat(testMealDiary.getCommentCount()).isEqualTo(-1);
    }


    @Test
    @DisplayName("존재하지 않는 댓글을 삭제하려고 하면 예외가 발생한다")
    public void delete_fail_commentNotFound() {
        // given
        MealDiaryCommentDeleteDTO.MealDiaryCommentDeleteRequestDTO request =
                MealDiaryCommentDeleteDTO.MealDiaryCommentDeleteRequestDTO.builder()
                        .userId(2L)
                        .mealDiaryCommentId(999L)
                        .build();

        when(mealDiaryCommentsRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> mealDiaryCommentService.delete(request))
                .isInstanceOf(MealDiaryCommentHandler.class)
                .hasFieldOrPropertyWithValue("code", ErrorStatus.COMMENT_NOT_FOUND);
    }


    @Test
    @DisplayName("댓글 작성자가 아닌 사용자가 삭제하려고 하면 예외가 발생한다")
    public void delete_fail_unauthorizedUser() {
        // given
        MealDiaryCommentDeleteDTO.MealDiaryCommentDeleteRequestDTO request =
                MealDiaryCommentDeleteDTO.MealDiaryCommentDeleteRequestDTO.builder()
                        .userId(1L)
                        .mealDiaryCommentId(1L)
                        .build();

        MealDiaryComments comment = MealDiaryComments.builder()
                .id(1L)
                .content("test_comment")
                .mealDiary(testMealDiary)
                .user(testMember2)
                .build();

        when(mealDiaryCommentsRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(testMember));

        // when & then
        assertThatThrownBy(() -> mealDiaryCommentService.delete(request))
                .isInstanceOf(UserEntityHandler.class)
                .hasFieldOrPropertyWithValue("code", ErrorStatus.MEMBER_AUTHENTICATE_FAILED);
    }


    @Test
    @DisplayName("댓글이 삭제되면 게시글의 댓글 카운트가 감소한다")
    public void delete_success_countDown() {
        // given
        MealDiaryCommentDeleteDTO.MealDiaryCommentDeleteRequestDTO request =
                MealDiaryCommentDeleteDTO.MealDiaryCommentDeleteRequestDTO.builder()
                        .userId(2L)
                        .mealDiaryCommentId(1L)
                        .build();

        MealDiaryComments comment = MealDiaryComments.builder()
                .id(1L)
                .content("test_comment")
                .mealDiary(testMealDiary)
                .user(testMember2)
                .build();

        testMealDiary.setCommentCount(1);

        when(mealDiaryCommentsRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(testMember2));
        doNothing().when(mealDiaryCommentsRepository).delete(any(MealDiaryComments.class));

        // when
        mealDiaryCommentService.delete(request);

        // then
        assertThat(testMealDiary.getCommentCount()).isEqualTo(0);
        verify(mealDiaryCommentsRepository).delete(comment);
    }


    @Test
    @DisplayName("update()를 이용해서 댓글 내용을 수정할 수 있다")
    public void update_success() {
        // given
        MealDiaryCommentUpdateDTO.MealDiaryCommentUpdateRequestDTO request =
                MealDiaryCommentUpdateDTO.MealDiaryCommentUpdateRequestDTO.builder()
                        .userId(2L)
                        .mealDiaryCommentId(1L)
                        .comment("updated_comment")
                        .build();

        MealDiaryComments originalComment = MealDiaryComments.builder()
                .id(1L)
                .content("original_comment")
                .mealDiary(testMealDiary)
                .user(testMember2)
                .build();

        MealDiaryComments updatedComment = MealDiaryComments.builder()
                .id(1L)
                .content("updated_comment")
                .mealDiary(testMealDiary)
                .user(testMember2)
                .build();

        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(testMember2));
        when(mealDiaryCommentsRepository.findById(anyLong())).thenReturn(Optional.of(originalComment));
        when(mealDiaryCommentsRepository.save(any(MealDiaryComments.class))).thenReturn(updatedComment);

        // when
        MealDiaryCommentUpdateDTO.MealDiaryCommentUpdateResponseDTO response =
                mealDiaryCommentService.update(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getMealDiaryCommentId()).isEqualTo(1L);
        verify(mealDiaryCommentsRepository).findById(1L);
        verify(userEntityRepository).findById(2L);
        verify(mealDiaryCommentsRepository).save(any(MealDiaryComments.class));
    }


    @Test
    @DisplayName("존재하지 않는 댓글을 수정하려고 하면 예외가 발생한다")
    public void update_fail_commentNotFound() {
        // given
        MealDiaryCommentUpdateDTO.MealDiaryCommentUpdateRequestDTO request =
                MealDiaryCommentUpdateDTO.MealDiaryCommentUpdateRequestDTO.builder()
                        .userId(2L)
                        .mealDiaryCommentId(999L)
                        .comment("updated_comment")
                        .build();

        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(testMember2));
        when(mealDiaryCommentsRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> mealDiaryCommentService.update(request))
                .isInstanceOf(MealDiaryCommentHandler.class)
                .hasFieldOrPropertyWithValue("code", ErrorStatus.COMMENT_NOT_FOUND);
    }


    @Test
    @DisplayName("존재하지 않는 사용자가 댓글을 수정하려고 하면 예외가 발생한다")
    public void update_fail_userNotFound() {
        // given
        MealDiaryCommentUpdateDTO.MealDiaryCommentUpdateRequestDTO request =
                MealDiaryCommentUpdateDTO.MealDiaryCommentUpdateRequestDTO.builder()
                        .userId(999L)
                        .mealDiaryCommentId(1L)
                        .comment("updated_comment")
                        .build();

        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> mealDiaryCommentService.update(request))
                .isInstanceOf(UserEntityHandler.class)
                .hasFieldOrPropertyWithValue("code", ErrorStatus.MEMBER_NOT_FOUND);
    }


    @Test
    @DisplayName("댓글 작성자가 아닌 사용자가 수정하려고 하면 예외가 발생한다")
    public void update_fail_unauthorizedUser() {
        // given
        MealDiaryCommentUpdateDTO.MealDiaryCommentUpdateRequestDTO request =
                MealDiaryCommentUpdateDTO.MealDiaryCommentUpdateRequestDTO.builder()
                        .userId(1L)
                        .mealDiaryCommentId(1L)
                        .comment("updated_comment")
                        .build();

        MealDiaryComments originalComment = MealDiaryComments.builder()
                .id(1L)
                .content("original_comment")
                .mealDiary(testMealDiary)
                .user(testMember2)
                .build();

        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(testMember));
        when(mealDiaryCommentsRepository.findById(anyLong())).thenReturn(Optional.of(originalComment));

        // when & then
        assertThatThrownBy(() -> mealDiaryCommentService.update(request))
                .isInstanceOf(UserEntityHandler.class)
                .hasFieldOrPropertyWithValue("code", ErrorStatus.MEMBER_AUTHENTICATE_FAILED);
    }
}