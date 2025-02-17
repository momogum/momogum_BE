package com.example.momogum.service.mealDiaryService;

import com.example.momogum.apiPayLoad.exception.handler.MealDiaryHandler;
import com.example.momogum.apiPayLoad.exception.handler.MealDiaryStoryHandler;
import com.example.momogum.apiPayLoad.exception.handler.UserEntityHandler;
import com.example.momogum.domain.*;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.momogum.apiPayLoad.code.status.ErrorStatus.*;
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
    UserEntity testMember2;
    MealDiary testMealDiary;
    MealDiaryStory testMealDiaryStory;
    MealDiaryImage testMealDiaryImage;


    @BeforeEach
    void setUp() {

        // 회원 설정 - 프로필 이미지는 일부러 넣지 않음
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

        testMember2 = UserEntity.builder()
                .id(2L)
                .phoneNumber("test_phone_number2")
                .name("test_name2")
                .nickname("test_nickname2")
                .about("test_about2")
                .provider(LoginType.KAKAO)
                .providerId("test_provider_id2")
                .followerCount(0)
                .followingCount(0)
                .build();

        // 2번회원이 1번회원을 팔로우하도록
        Following.builder()
                .id(1L)
                .user(testMember2)
                .following(testMember)
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

        testMealDiaryImage.setMealDiary(testMealDiary);

        testMealDiaryStory = MealDiaryStory.builder()
                .id(1L)
                .name(testMember.getNickname())
                .mealDiary(testMealDiary)
                .build();

        spy(testMealDiaryStory);
        spy(testMealDiary);
    }


    @Test
    @DisplayName("get()을 이용하여 스토리를 상세 조회 할 수 있다")
    public void get_success() {
        // given
        when(userEntityRepository.findById(any())).thenReturn(Optional.ofNullable(testMember));
        when(mealDiaryStoryRepository.findById(any())).thenReturn(Optional.ofNullable(testMealDiaryStory));

        // when
        MealDiaryStoryReadDTO.MealDiaryStoryReadResponseDTO response = mealDiaryStoryService.get(1L, 1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(testMember.getNickname());
        assertThat(response.getMealDiaryImageLinks()).isEqualTo(List.of(testMealDiaryImage.getImageLink()));
        assertThat(response.getProfileImageLink()).isEqualTo(null);
        assertThat(response.getLocation()).isEqualTo(testMealDiary.getLocation());
        assertThat(response.getDescription()).isEqualTo(testMealDiary.getDescription());
    }


    @Test
    @DisplayName("스토리를 조회하면 스토리 조회 엔티티를 생성한다")
    public void get_save_mealDiaryStoryView(){
        // given
        when(userEntityRepository.findById(any())).thenReturn(Optional.ofNullable(testMember));
        when(mealDiaryStoryRepository.findById(any())).thenReturn(Optional.ofNullable(testMealDiaryStory));

        // when
        mealDiaryStoryService.get(1L, 1L);

        //then
        verify(mealDiaryStoryViewRepository, times(1)).save(any());
    }


    @Test
    @DisplayName("존재하지 않는 회원이 스토리를 조회하면 정해진 예외를 반환한다")
    public void get_fail_memberNotFound(){
        //given
        when(userEntityRepository.findById(any())).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> mealDiaryStoryService.get(1L, 1L))
                .isInstanceOf(UserEntityHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_NOT_FOUND);
    }


    @Test
    @DisplayName("존재하지 않는 밥일기를 조회하면 정해진 예외를 반환한다")
    public void get_fail_mealDiaryNotFound(){
        //given
        when(userEntityRepository.findById(any())).thenReturn(Optional.ofNullable(testMember));
        when(mealDiaryStoryRepository.findById(any())).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> mealDiaryStoryService.get(1L, 1L))
                .isInstanceOf(MealDiaryStoryHandler.class)
                .hasFieldOrPropertyWithValue("code", MEALDIARY_STORY_NOT_FOUND);
    }


    //FIXME
    @Test
    @DisplayName("회원의 프로필 이미지가 저장되어 있지 않으면 null을 반환한다" +
            "회원가입 시, 기본이미지를 저장하는 로직이 구현되면 이 테스트도 예외를 반환하도록 수정해야함")
    public void get_fail_memberProfileImage_null(){
        // given
        when(userEntityRepository.findById(any())).thenReturn(Optional.ofNullable(testMember));
        when(mealDiaryStoryRepository.findById(any())).thenReturn(Optional.ofNullable(testMealDiaryStory));

        // when
        MealDiaryStoryReadDTO.MealDiaryStoryReadResponseDTO response = mealDiaryStoryService.get(1L, 1L);

        // then
        assertThat(response.getProfileImageLink()).isEqualTo(null);
    }


    @Test
    @DisplayName("getAll()을 이용하여 회원이 팔로우하고 있는 회원들의 스토리를 조회 할 수 있다")
    public void getAll_success(){
        //given
        MealDiaryStoryView mealDiaryStoryView = MealDiaryStoryView.builder()
                .id(1L)
                .isViewed(true)
                .mealDiaryStory(testMealDiaryStory)
                .userEntity(testMember)
                .build();

        when(userEntityRepository.findById(any())).thenReturn(Optional.ofNullable(testMember2));
        when(followingRepository.findFollowedUsersByUserId(any())).thenReturn(List.of(testMember));
        when(mealDiaryRepository.findByUserEntityIn(any())).thenReturn(List.of(testMealDiary));
        when(mealDiaryStoryRepository.findByMealDiaryIn(any())).thenReturn(List.of(testMealDiaryStory));
        when(mealDiaryStoryViewRepository.findByUserEntityAndMealDiaryStory(any(),any())).thenReturn(mealDiaryStoryView);

        //when
        List<MealDiaryStoryReadDTO.MealDiaryStoryReadAllResponseDTO> response = mealDiaryStoryService.getAll(2L);

        //then
        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(1);
        assertThat(response.get(0).getMealDiaryStoryId()).isEqualTo(testMealDiaryImage.getId());
        assertThat(response.get(0).getNickname()).isEqualTo(testMember.getNickname());
        assertThat(response.get(0).getProfileImageLink()).isEqualTo(null);
        assertThat(response.get(0).getMealDiaryImageLinks()).isEqualTo(testMealDiaryImage.getImageLink());
        assertThat(response.get(0).isViewed()).isEqualTo(true);
    }


    @Test
    @DisplayName("회원이 팔로우하고 있는 회원이 없다면 빈 리스트를 반환한다" +
            "예외는 반환하지 않는다")
    public void getAll_success_follow_none(){
        //given
        when(userEntityRepository.findById(any())).thenReturn(Optional.ofNullable(testMember2));
        when(followingRepository.findFollowedUsersByUserId(any())).thenReturn(List.of());

        //when
        List<MealDiaryStoryReadDTO.MealDiaryStoryReadAllResponseDTO> response = mealDiaryStoryService.getAll(2L);

        //then
        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(0);
    }


    @Test
    @DisplayName("팔로우하고 있는 회원들의 밥일기가 존재하지 않으면 빈 리스트를 반환한다" +
            "예외는 반환하지 않는다")
    public void getAll_success_mealDiary_none(){
        //given
        when(userEntityRepository.findById(any())).thenReturn(Optional.ofNullable(testMember2));
        when(followingRepository.findFollowedUsersByUserId(any())).thenReturn(List.of(testMember));
        when(mealDiaryRepository.findByUserEntityIn(any())).thenReturn(List.of());

        //when
        List<MealDiaryStoryReadDTO.MealDiaryStoryReadAllResponseDTO> response = mealDiaryStoryService.getAll(2L);

        //then
        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(0);
    }


    @Test
    @DisplayName("팔로우하고 있는 회원들의 스토리가 존재하지 않으면 빈 리스트를 반환한다" +
            "예외는 반환하지 않는다")
    public void getAll_success_mealDiaryStory_none(){
        //given
        when(userEntityRepository.findById(any())).thenReturn(Optional.ofNullable(testMember2));
        when(followingRepository.findFollowedUsersByUserId(any())).thenReturn(List.of(testMember));
        when(mealDiaryRepository.findByUserEntityIn(any())).thenReturn(List.of(testMealDiary));
        when(mealDiaryStoryRepository.findByMealDiaryIn(any())).thenReturn(List.of());

        //when
        List<MealDiaryStoryReadDTO.MealDiaryStoryReadAllResponseDTO> response = mealDiaryStoryService.getAll(2L);

        //then
        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(0);
    }


    @Test
    @DisplayName("존재하지 않는 회원이 스토리를 조회하면 정해진 예외를 반환한다")
    public void getAll_fail_memberNotFound(){
        //given
        when(userEntityRepository.findById(any())).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> mealDiaryStoryService.getAll(2L))
                .isInstanceOf(UserEntityHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_NOT_FOUND);
    }


    // FIXME
    @Test
    @DisplayName("스토리를 올린 회원의 프로필 사진이 없는 경우 프로필 사진에 null을 반환한다" +
            "이것도 기본이미지 로직이 구현되면 에러가 반환되도록 테스트 작성해야함")
    public void getAll_fail_ProfileImage_none(){
        when(userEntityRepository.findById(any())).thenReturn(Optional.ofNullable(testMember2));
        when(followingRepository.findFollowedUsersByUserId(any())).thenReturn(List.of(testMember));
        when(mealDiaryRepository.findByUserEntityIn(any())).thenReturn(List.of(testMealDiary));
        when(mealDiaryStoryRepository.findByMealDiaryIn(any())).thenReturn(List.of(testMealDiaryStory));

        //when
        List<MealDiaryStoryReadDTO.MealDiaryStoryReadAllResponseDTO> response = mealDiaryStoryService.getAll(2L);

        //then
        assertThat(response.get(0).getProfileImageLink()).isEqualTo(null);
    }


    @Test
    @DisplayName("스토리를 조회한 적이 없다면 isViewed는 false를 반환한다")
    public void getAll_success_isViewed_false(){
        when(userEntityRepository.findById(any())).thenReturn(Optional.ofNullable(testMember2));
        when(followingRepository.findFollowedUsersByUserId(any())).thenReturn(List.of(testMember));
        when(mealDiaryRepository.findByUserEntityIn(any())).thenReturn(List.of(testMealDiary));
        when(mealDiaryStoryRepository.findByMealDiaryIn(any())).thenReturn(List.of(testMealDiaryStory));

        //when
        List<MealDiaryStoryReadDTO.MealDiaryStoryReadAllResponseDTO> response = mealDiaryStoryService.getAll(2L);

        //then
        assertThat(response.get(0).isViewed()).isFalse();
    }


    @Test
    @DisplayName("스토리는 isViewed가 false인 것부터 조회된다")
    public void getAll_success_isViewed_true(){
        //given
        MealDiaryStory testMealDiaryStory1 = MealDiaryStory.builder()
                .id(1L)
                .name(testMember.getNickname())
                .mealDiary(testMealDiary)
                .build();

        MealDiaryStory testMealDiaryStory2 = MealDiaryStory.builder()
                .id(2L)
                .name(testMember.getNickname())
                .mealDiary(testMealDiary)
                .build();

        testMealDiaryStory1.setCreatedAt(LocalDateTime.now());
        testMealDiaryStory2.setCreatedAt(LocalDateTime.now());

        MealDiaryStoryView mealDiaryStoryView = MealDiaryStoryView.builder()
                .id(1L)
                .isViewed(true)
                .mealDiaryStory(testMealDiaryStory2)
                .userEntity(testMember)
                .build();

        System.out.println("1번 생성일자: "+testMealDiaryStory1.getCreatedAt()+" / 2번 생성일자: "+testMealDiaryStory2.getCreatedAt());

        when(userEntityRepository.findById(any())).thenReturn(Optional.ofNullable(testMember));
        when(followingRepository.findFollowedUsersByUserId(any())).thenReturn(List.of(testMember));
        when(mealDiaryRepository.findByUserEntityIn(any())).thenReturn(List.of(testMealDiary));
        when(mealDiaryStoryRepository.findByMealDiaryIn(any())).thenReturn(List.of(testMealDiaryStory1,testMealDiaryStory2));

        lenient().when(mealDiaryStoryViewRepository.findByUserEntityAndMealDiaryStory(testMember, testMealDiaryStory1))
                .thenReturn(null);
        lenient().when(mealDiaryStoryViewRepository.findByUserEntityAndMealDiaryStory(testMember, testMealDiaryStory2))
                .thenReturn(mealDiaryStoryView);

        //when
        List<MealDiaryStoryReadDTO.MealDiaryStoryReadAllResponseDTO> response = mealDiaryStoryService.getAll(2L);

        //then
        assertThat(response.size()).isEqualTo(2);
        assertThat(response.get(0).getMealDiaryStoryId()).isEqualTo(1L);
        assertThat(response.get(1).getMealDiaryStoryId()).isEqualTo(2L);
    }


    @Test
    @DisplayName("스토리는 먼저 작성 일시에 따라 내림차순으로 조회된다")
    public void getAll_success_createdAt(){
        //given
        MealDiaryStory testMealDiaryStory2 = MealDiaryStory.builder()
                .id(2L)
                .name(testMember.getNickname())
                .mealDiary(testMealDiary)
                .build();

        testMealDiaryStory.setCreatedAt(LocalDateTime.now());
        testMealDiaryStory2.setCreatedAt(LocalDateTime.now().minusDays(1));
        System.out.println(testMealDiaryStory.getCreatedAt()+"   "+testMealDiaryStory2.getCreatedAt());

        when(userEntityRepository.findById(any())).thenReturn(Optional.ofNullable(testMember));
        when(followingRepository.findFollowedUsersByUserId(any())).thenReturn(List.of(testMember));
        when(mealDiaryRepository.findByUserEntityIn(any())).thenReturn(List.of(testMealDiary));
        when(mealDiaryStoryRepository.findByMealDiaryIn(any())).thenReturn(List.of(testMealDiaryStory,testMealDiaryStory2));

        //when
        List<MealDiaryStoryReadDTO.MealDiaryStoryReadAllResponseDTO> response = mealDiaryStoryService.getAll(2L);

        //then
        assertThat(response.size()).isEqualTo(2);
        assertThat(response.get(0).getMealDiaryStoryId()).isEqualTo(1L);
        assertThat(response.get(1).getMealDiaryStoryId()).isEqualTo(2L);
    }


    @Test
    @DisplayName("작성일시보다, 조회 여부가 우선시 조회된다")
    public void getAll_success_createdAt_viewed(){
        //given
        testMealDiaryStory = MealDiaryStory.builder()
                .id(1L)
                .name(testMember.getNickname())
                .mealDiary(testMealDiary)
                .build();

        MealDiaryStory testMealDiaryStory2 = MealDiaryStory.builder()
                .id(2L)
                .name(testMember.getNickname())
                .mealDiary(testMealDiary)
                .build();

        testMealDiaryStory2.setCreatedAt(LocalDateTime.now().minusDays(1));
        testMealDiaryStory.setCreatedAt(LocalDateTime.now());

        MealDiaryStoryView mealDiaryStoryView = MealDiaryStoryView.builder()
                .id(1L)
                .isViewed(true)
                .mealDiaryStory(testMealDiaryStory2)
                .userEntity(testMember)
                .build();

        System.out.println(testMealDiaryStory.getCreatedAt()+"   "+testMealDiaryStory2.getCreatedAt());

        when(userEntityRepository.findById(any())).thenReturn(Optional.ofNullable(testMember2));
        when(followingRepository.findFollowedUsersByUserId(any())).thenReturn(List.of(testMember));
        when(mealDiaryRepository.findByUserEntityIn(any())).thenReturn(List.of(testMealDiary));
        when(mealDiaryStoryRepository.findByMealDiaryIn(any())).thenReturn(List.of(testMealDiaryStory,testMealDiaryStory2));

        lenient().when(mealDiaryStoryViewRepository.findByUserEntityAndMealDiaryStory(testMember, testMealDiaryStory2))
                .thenReturn(mealDiaryStoryView);
        lenient().when(mealDiaryStoryViewRepository.findByUserEntityAndMealDiaryStory(testMember, testMealDiaryStory))
                .thenReturn(null);

        //when
        List<MealDiaryStoryReadDTO.MealDiaryStoryReadAllResponseDTO> response = mealDiaryStoryService.getAll(2L);

        //then
        assertThat(response.size()).isEqualTo(2);
        assertThat(response.get(0).getMealDiaryStoryId()).isEqualTo(1L);
        assertThat(response.get(1).getMealDiaryStoryId()).isEqualTo(2L);
    }
}