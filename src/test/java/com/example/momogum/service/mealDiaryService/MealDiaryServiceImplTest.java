package com.example.momogum.service.mealDiaryService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.handler.ImageHandler;
import com.example.momogum.apiPayLoad.exception.handler.MealDiaryHandler;
import com.example.momogum.apiPayLoad.exception.handler.UserEntityHandler;
import com.example.momogum.domain.*;
import com.example.momogum.domain.common.enums.FoodCategory;
import com.example.momogum.domain.common.enums.IsRevisit;
import com.example.momogum.domain.common.enums.ReportReason;
import com.example.momogum.repository.mealDiaryBookmarkRepo.MealDiaryBookmarkRepository;
import com.example.momogum.repository.mealDiaryCommentsRepo.MealDiaryCommentsRepository;
import com.example.momogum.repository.mealDiaryRepo.*;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.mealDiary.MealDairiesDTO;
import com.example.momogum.web.dto.mealDiary.MealDiaryReportDTO;
import com.example.momogum.web.dto.mealDiary.MealDiaryUpdateDTO;
import org.apache.catalina.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.example.momogum.apiPayLoad.code.status.ErrorStatus.*;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MealDiaryServiceImplTest {

    @InjectMocks
    MealDiaryServiceImpl mealDiaryService;

    @Mock
    MealDiaryRepository mealDiaryRepository;

    @Mock
    UserEntityRepository userEntityRepository;

    @Mock
    MealDiaryKeywordRepository mealDiaryKeywordRepository;

    @Mock
    KeywordRepository keywordRepository;

    @Mock
    MealDiaryImageUtil mealDiaryImageUtil;

    @Mock
    MealDiaryCommentsRepository mealDiaryCommentsRepository;

    @Mock
    MealDiaryLikesRepository mealDiaryLikesRepository;

    @Mock
    MealDiaryBookmarkRepository mealDiaryBookmarkRepository;

    @Mock
    MealDiaryReportRepository mealDiaryReportRepository;

    @Mock
    MealDiaryStoryRepository mealDiaryStoryRepository;

    @Mock
    List<MultipartFile> files;

    UserEntity testMember;

    @Mock
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
    @DisplayName("save를 이용해서 밥일기를 저장 할 수 있다")
    public void save_success(){
        //given
        MealDairiesDTO.CreateStoryRequestDTO request = MealDairiesDTO.CreateStoryRequestDTO.builder()
                .memberId(1L)
                .foodCategory(FoodCategory.FAST_FOOD)
                .keyword("한식,중식,양식")
                .location("test_location")
                .description("test_description")
                .revisit(IsRevisit.GOOD)
                .build();

        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[]{1, 2, 3});
        List<MultipartFile> files = List.of(file);

        MealDiary savedMealDiary = MealDiary.builder()
                        .id(1L)
                                .build();

        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(testMember));
        when(mealDiaryRepository.save(any())).thenReturn(savedMealDiary);

        //when
        MealDairiesDTO.CreateStoryResponseDTO response = mealDiaryService.save(request, files);
        System.out.println(response.getMealDiaryId());

        //then
        assertThat(response).isNotNull();
        assertThat(response.getMealDiaryId()).isEqualTo(1L);

        verify(mealDiaryRepository, times(1)).save(any());

        /**
         * 일반 객체에서 데이터를 get하는 로직은 모킹하는데에 어려움이 있습니다.
         * 객체를 생성할때 빌더 패턴을 통해 실제 빌더 메서드가 호출되면서,
         * 프록시 객체를 이용하는게 아닌 실제 객체가 생성되어버리기 때문입니다. (실제 객체는 모킹 할 수 없습니다)
         *
         * 이를 하려면 1차원적으로는 객체를 따로 생성하지 않고
         *     when(testMealDiary.getLocation()).thenReturn("test_location");
         * 이런식으로 할 수도 있겠지만,
         * 객체에서 참조하는 그림이 아니기 때문에 제 마음엔 들지 않는군요
         *
         * 그러면 객체에서 데이터를 꺼내는 로직은 어떻게 모킹 할 수 있을까요?
         *
         * 1. spy를 이용하여 해당 객체를 실제 객체로 사용한다
         *  이렇게 되면 해당 객체는 더이상 프록시 객체가 아닌 실제 객체가 됩니다 -> 즉 모킹이 안됩니다
         *  해당 테스트에서는 save의 반환값으로서 모킹이 필요한 데 spy로 감싸게 되면 이 부분이 불가능해져서 spy로 사용 할 수 없습니다
         *  즉 문제가 해결되지 않는다는 얘기죠
         *
         * 2. spy로서 사용되는 객체를 따로 생성한다
         *  이렇게 하면 객체의 메서드를
         *  doReturn(1L).when(testMealDiary).getId();
         *  과 같이 사용 할 수 있습니다.
         *
         * 3. 응답만을 생성하는 Mock 객체를 따로 생성한다
         *  그냥 응답만을 위한 객체를 따로 생성하는 것입니다.
         *  저는 이 방법이 가장 편하다고 생각하여 이 방법을 통해 테스트를 구현하였습니다
         *
         * 그래서 결론적으로 1,2,3의 방법이 필요한 것 입니다.
         * */
    }


    @Test
    @DisplayName("존재하지 않는 회원이 밥일기 작성을 시도하면 정해진 예외를 반환한다")
    public void save_fail_memberNotFound(){
        //given
        MealDairiesDTO.CreateStoryRequestDTO request = MealDairiesDTO.CreateStoryRequestDTO.builder()
                .memberId(1L)
                .foodCategory(FoodCategory.FAST_FOOD)
                .keyword("한식,중식,양식")
                .location("test_location")
                .description("test_description")
                .revisit(IsRevisit.GOOD)
                .build();

        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[]{1, 2, 3});
        List<MultipartFile> files = List.of(file);

        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> mealDiaryService.save(request, files))
                .isInstanceOf(UserEntityHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_NOT_FOUND);
    }


    @Test
    @DisplayName("밥일기를 저장하면 스토리도 동시에 생성되어 저장된다")
    public void save_success_withStory(){
        //given
        MealDairiesDTO.CreateStoryRequestDTO request = MealDairiesDTO.CreateStoryRequestDTO.builder()
                .memberId(1L)
                .foodCategory(FoodCategory.FAST_FOOD)
                .keyword("한식,중식,양식")
                .location("test_location")
                .description("test_description")
                .revisit(IsRevisit.GOOD)
                .build();

        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[]{1, 2, 3});
        List<MultipartFile> files = List.of(file);

        MealDiary savedMealDiary = MealDiary.builder()
                .id(1L)
                .build();

        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(testMember));
        when(mealDiaryRepository.save(any())).thenReturn(savedMealDiary);

        //when
        MealDairiesDTO.CreateStoryResponseDTO response = mealDiaryService.save(request, files);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getMealDiaryId()).isEqualTo(1L);

        verify(mealDiaryStoryRepository, times(1)).save(any());
    }


    @Test
    @DisplayName("이미지 업로드 중 오류가 발생하면 정해진 예외를 반환한다")
    public void save_fail_imageUploadError(){
        //given
        MealDairiesDTO.CreateStoryRequestDTO request = MealDairiesDTO.CreateStoryRequestDTO.builder()
                .memberId(1L)
                .foodCategory(FoodCategory.FAST_FOOD)
                .keyword("한식,중식,양식")
                .location("test_location")
                .description("test_description")
                .revisit(IsRevisit.GOOD)
                .build();

        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[]{1, 2, 3});
        List<MultipartFile> files = List.of(file);

        MealDiary savedMealDiary = MealDiary.builder()
                .id(1L)
                .build();

        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(testMember));
        when(mealDiaryRepository.save(any())).thenReturn(savedMealDiary);
        when(mealDiaryImageUtil.uploadImages(any(),any(),any())).thenThrow(new ImageHandler(IMAGE_UPLOAD_ERROR));

        //when & then
        assertThatThrownBy(() -> mealDiaryService.save(request, files))
                .isInstanceOf(ImageHandler.class)
                .hasFieldOrPropertyWithValue("code", IMAGE_UPLOAD_ERROR);
    }


    @Test
    @DisplayName("extractedKeyword()를 이용하여 키워드를 , 기준으로 파싱 할 수 있다" +
            "밥일기와 키워드의 매핑테이블을 생성 할 수 있다")
    public void extractedKeyword_success(){
        //given
        MealDairiesDTO.CreateStoryRequestDTO request = MealDairiesDTO.CreateStoryRequestDTO.builder()
                .memberId(1L)
                .foodCategory(FoodCategory.FAST_FOOD)
                .keyword("한식,중식,양식")
                .location("test_location")
                .description("test_description")
                .revisit(IsRevisit.GOOD)
                .build();

        //when
        mealDiaryService.extractedKeyword(request,testMealDiary);

        //then: 키워드가 3개이므로 3번 저장이 호출되어야 함
        verify(keywordRepository, times(3)).save(any());
        verify(mealDiaryKeywordRepository, times(3)).save(any());
    }


    @Test
    @DisplayName("이미 존재하는 키워드의 경우 저장하지 않는다" +
            "매핑테이블의 경우 이미 존재하는 키워드를 통해 생성 할 수 있다")
    public void extractedKeyword_success_keywordExist(){
        //given
        MealDairiesDTO.CreateStoryRequestDTO request = MealDairiesDTO.CreateStoryRequestDTO.builder()
                .memberId(1L)
                .foodCategory(FoodCategory.FAST_FOOD)
                .keyword("한식,중식,양식")
                .location("test_location")
                .description("test_description")
                .revisit(IsRevisit.GOOD)
                .build();

        Keyword testKeyword = Keyword.builder()
                .id(1L)
                .keyword("한식")
                .mealDiaryKeywords(List.of())
                .build();

        when(keywordRepository.findByKeyword("한식")).thenReturn(Optional.of(testKeyword));

        //when
        mealDiaryService.extractedKeyword(request,testMealDiary);

        //then
        verify(keywordRepository, times(2)).save(any());
        verify(mealDiaryKeywordRepository, times(3)).save(any());
    }


    @Test
    @DisplayName("키워드가 5개를 초과하여 입력되면 정해진 예외를 반환한다")
    public void extractedKeyword_fail(){
        //given
        MealDairiesDTO.CreateStoryRequestDTO request = MealDairiesDTO.CreateStoryRequestDTO.builder()
                .memberId(1L)
                .foodCategory(FoodCategory.FAST_FOOD)
                .keyword("한식,중식,양식,패스트푸드,카페,에러")
                .location("test_location")
                .description("test_description")
                .revisit(IsRevisit.GOOD)
                .build();

        //when & then
        assertThatThrownBy(() -> mealDiaryService.extractedKeyword(request, testMealDiary))
                .isInstanceOf(MealDiaryHandler.class)
                .hasFieldOrPropertyWithValue("code", MEALDIARY_KEYWORD_MAX);
    }


    @Test
    @DisplayName("get()을 이용하여 밥일기를 조회 할 수 있다")
    public void get_success(){
        //given
        String imageLink = "test_image_link";
        UserEntity spyMember = spy(testMember);

        ProfileImage testProfileImage = ProfileImage.builder()
                .id(1L)
                .user(spyMember)
                .imageLink("test_profile_image_link")
                .fileName("test_file_name")
                .imageName("test_image_name")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        MealDiary realMealDiary = MealDiary.builder()
                .foodCategory(FoodCategory.FAST_FOOD)
                .location("test_location")
                .description("test_description")
                .isRevisit(IsRevisit.GOOD)
                .isReport(false)
                .likesCount(0)
                .commentCount(0)
                .userEntity(spyMember)
                .mealDiaryImages(null)
                .mealDiaryKeywords(new ArrayList<>())
                .build();

        MealDiary testMealDiary = spy(realMealDiary);

        Keyword testKeyword1 = Keyword.builder()
                .id(1L)
                .keyword("한식")
                .build();

        MealDiaryKeyword testMealDiaryKeyword = MealDiaryKeyword.builder()
                .keyword(testKeyword1)
                .mealDiary(testMealDiary)
                .build();

        List<MealDiaryKeyword> mealDiaryKeywords = new ArrayList<>();
        mealDiaryKeywords.add(testMealDiaryKeyword);

        doReturn(mealDiaryKeywords).when(testMealDiary).getMealDiaryKeywords();
        doReturn(testProfileImage).when(spyMember).getProfileImage();

        when(mealDiaryImageUtil.findImagesByMealId(any())).thenReturn(List.of(imageLink));
        when(mealDiaryRepository.findById(anyLong())).thenReturn(Optional.of(testMealDiary));
        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(spyMember));
        when(mealDiaryCommentsRepository.findByMealDiaryId(any())).thenReturn(List.of());
        when(mealDiaryLikesRepository.existsByUserEntityAndMealDiary(any(), any())).thenReturn(false);
        when(mealDiaryBookmarkRepository.existsByUserEntityAndMealDiary(any(), any())).thenReturn(false);

        //when
        MealDairiesDTO.GetMealDiaryResponseDTO response = mealDiaryService.get(1L, 1L);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getNickname()).isEqualTo("test");
        assertThat(response.getMealDiaryImageLinks()).isEqualTo(List.of("test_image_link"));
        assertThat(response.getMealDiaryLikeCount()).isEqualTo(0);
        assertThat(response.getMealDiaryCommentCount()).isEqualTo(0);
        assertThat(response.isMealDairyBookmark()).isFalse();
        assertThat(response.getKeywords()).contains("한식");
        assertThat(response.getLocation()).isEqualTo("test_location");

        assertThat(response.getUserProfileImageLink()).isEqualTo("test_profile_image_link");
        assertThat(response.getComments()).isEqualTo(List.of());
    }


    //FIXME
    @Test
    @DisplayName("회원 프로필 이미지가 없으면 기본이미지를 반환한다" +
            "추후 기본이미지 로직 정확히 구현 시, 예외로 수정")
    public void get_success_profile_image(){
        //given
        String imageLink = "test_image_link";

        MealDiary realMealDiary = MealDiary.builder()
                .foodCategory(FoodCategory.FAST_FOOD)
                .location("test_location")
                .description("test_description")
                .isRevisit(IsRevisit.GOOD)
                .isReport(false)
                .likesCount(0)
                .commentCount(0)
                .userEntity(testMember)
                .mealDiaryImages(null)
                .mealDiaryKeywords(new ArrayList<>())
                .build();
        MealDiary testMealDiary = spy(realMealDiary);

        Keyword testKeyword1 = Keyword.builder()
                .id(1L)
                .keyword("한식")
                .build();

        MealDiaryKeyword testMealDiaryKeyword = MealDiaryKeyword.builder()
                .keyword(testKeyword1)
                .mealDiary(testMealDiary)
                .build();

        List<MealDiaryKeyword> mealDiaryKeywords = new ArrayList<>();
        mealDiaryKeywords.add(testMealDiaryKeyword);

        doReturn(mealDiaryKeywords).when(testMealDiary).getMealDiaryKeywords();

        when(mealDiaryImageUtil.findImagesByMealId(any())).thenReturn(List.of(imageLink));
        when(mealDiaryRepository.findById(anyLong())).thenReturn(Optional.of(testMealDiary));
        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(testMember));
        when(mealDiaryCommentsRepository.findByMealDiaryId(any())).thenReturn(List.of());
        when(mealDiaryLikesRepository.existsByUserEntityAndMealDiary(any(), any())).thenReturn(false);
        when(mealDiaryBookmarkRepository.existsByUserEntityAndMealDiary(any(), any())).thenReturn(false);

        //when
        MealDairiesDTO.GetMealDiaryResponseDTO response = mealDiaryService.get(1L, 1L);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getUserProfileImageLink()).isEqualTo("default_image");
    }


    @Test
    @DisplayName("식별자에 해당하는 밥일기가 존재하지 않으면 정해진 예외를 반환한다")
    public void get_fail_mealDiaryNotFound(){
        //given
        String imageLink = "test_image_link";

        when(mealDiaryImageUtil.findImagesByMealId(any())).thenReturn(List.of(imageLink));
        when(mealDiaryRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> mealDiaryService.get(1L, 1L))
                .isInstanceOf(MealDiaryHandler.class)
                .hasFieldOrPropertyWithValue("code", MEALDIARY_NOT_FOUND);
    }


    @Test
    @DisplayName("getKeywords()를 이용해서 밥일기와 매핑된 키워드들을 String으로 반환 할 수 있다")
    public void getKeywords_success(){
        //given
        MealDiary realMealDiary = MealDiary.builder()
                .foodCategory(FoodCategory.FAST_FOOD)
                .location("test_location")
                .description("test_description")
                .isRevisit(IsRevisit.GOOD)
                .isReport(false)
                .likesCount(0)
                .commentCount(0)
                .userEntity(testMember)
                .mealDiaryImages(null)
                .mealDiaryKeywords(new ArrayList<>())
                .build();
        MealDiary testMealDiary = spy(realMealDiary);

        Keyword testKeyword1 = Keyword.builder()
                .id(1L)
                .keyword("한식")
                .build();

        Keyword testKeyword2 = Keyword.builder()
                .id(2L)
                .keyword("중식")
                .build();

        MealDiaryKeyword testMealDiaryKeyword = MealDiaryKeyword.builder()
                .keyword(testKeyword1)
                .mealDiary(testMealDiary)
                .build();

        MealDiaryKeyword testMealDiaryKeyword2 = MealDiaryKeyword.builder()
                .keyword(testKeyword2)
                .mealDiary(testMealDiary)
                .build();

        List<MealDiaryKeyword> mealDiaryKeywords = new ArrayList<>();
        mealDiaryKeywords.add(testMealDiaryKeyword);
        mealDiaryKeywords.add(testMealDiaryKeyword2);

        doReturn(mealDiaryKeywords).when(testMealDiary).getMealDiaryKeywords();

        //when
        List<String> response = mealDiaryService.getKeywords(testMealDiary);

        //then
        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(2);
        assertThat(response).isEqualTo(List.of("한식","중식"));
    }


    @Test
    @DisplayName("매핑된 키워드가 존재하지 않으면 공백을 반환한다")
    public void getKeywords_success_empty(){
        //given
        MealDiary realMealDiary = MealDiary.builder()
                .foodCategory(FoodCategory.FAST_FOOD)
                .location("test_location")
                .description("test_description")
                .isRevisit(IsRevisit.GOOD)
                .isReport(false)
                .likesCount(0)
                .commentCount(0)
                .userEntity(testMember)
                .mealDiaryImages(null)
                .mealDiaryKeywords(new ArrayList<>())
                .build();

        MealDiary testMealDiary = spy(realMealDiary);

        List<String> response = mealDiaryService.getKeywords(testMealDiary);

        //then
        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(0);
        assertThat(response).isEqualTo(List.of());
    }


    @Test
    @DisplayName("getAll()을 이용하여 자신이 올린 밥일기를 조회 할 수 있다")
    public void getAll_success(){
        // Given
        UserEntity spyMember = spy(testMember);

        MealDiary testMealDiary = MealDiary.builder()
                .foodCategory(FoodCategory.FAST_FOOD)
                .location("test_location")
                .description("test_description")
                .isRevisit(IsRevisit.GOOD)
                .isReport(false)
                .likesCount(0)
                .commentCount(0)
                .userEntity(spyMember)
                .mealDiaryImages(new ArrayList<>())
                .mealDiaryKeywords(new ArrayList<>())
                .build();

        MealDiary spyMealDiary = spy(testMealDiary);
        MealDiaryImage testMealDiaryImage = MealDiaryImage.builder()
                .id(1L)
                .imageLink("test_mealDiary_image_link")
                .mealDiary(spyMealDiary)
                .build();

        spyMealDiary.getMealDiaryImages().add(testMealDiaryImage);

        when(mealDiaryRepository.findByUserEntity(spyMember)).thenReturn(List.of(spyMealDiary));
        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(spyMember));

        // When
        List<MealDairiesDTO.GetAllMealDiaryResponseDTO> response = mealDiaryService.getAll(1L);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(1);
    }


    @Test
    @DisplayName("존재하지 않는 회원의 게시글을 조회하면 정해진 예외를 반환한다")
    public void getAll_fail_memberNotFound(){
        //given
        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> mealDiaryService.getAll(1L))
                .isInstanceOf(UserEntityHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_NOT_FOUND);
    }


    @Test
    @DisplayName("밥일기에 할당된 이미지가 없으면 정해진 예외를 반환한다")
    public void getAll_fail_mealDiaryImageNotFound(){
        //given
        UserEntity spyMember = spy(testMember);
        MealDiary testMealDiary = MealDiary.builder()
                .foodCategory(FoodCategory.FAST_FOOD)
                .location("test_location")
                .description("test_description")
                .isRevisit(IsRevisit.GOOD)
                .isReport(false)
                .likesCount(0)
                .commentCount(0)
                .userEntity(spyMember)
                .mealDiaryImages(new ArrayList<>())
                .mealDiaryKeywords(new ArrayList<>())
                .build();

        MealDiary spyMealDiary = spy(testMealDiary);

        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(spyMember));
        when(mealDiaryRepository.findByUserEntity(spyMember)).thenReturn(List.of(spyMealDiary));

        //when & then
        assertThatThrownBy(() -> mealDiaryService.getAll(1L))
                .isInstanceOf(ImageHandler.class)
                .hasFieldOrPropertyWithValue("code", IMAGE_NOT_FOUND);
    }


    @Test
    @DisplayName("delete()를 이용하여 밥일기를 삭제 할 수 있다")
    public void delete_success() throws FileNotFoundException {
        //given
        UserEntity spyMember = spy(testMember);

        MealDiary testMealDiary = MealDiary.builder()
                .foodCategory(FoodCategory.FAST_FOOD)
                .location("test_location")
                .description("test_description")
                .isRevisit(IsRevisit.GOOD)
                .isReport(false)
                .likesCount(0)
                .commentCount(0)
                .userEntity(spyMember)
                .mealDiaryImages(new ArrayList<>())
                .mealDiaryKeywords(new ArrayList<>())
                .build();

        MealDiary spyMealDiary = spy(testMealDiary);
        MealDiaryImage testMealDiaryImage = MealDiaryImage.builder()
                .id(1L)
                .imageLink("test_mealDiary_image_link")
                .mealDiary(spyMealDiary)
                .build();

        spyMealDiary.getMealDiaryImages().add(testMealDiaryImage);

        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(spyMember));
        when(mealDiaryRepository.findById(anyLong())).thenReturn(Optional.of(spyMealDiary));
        doNothing().when(mealDiaryImageUtil).deleteImage(any());

        //when
        mealDiaryService.delete(1L,1L);

        //then
        verify(mealDiaryImageUtil, times(1)).deleteImage(any());
        verify(mealDiaryRepository, times(1)).delete(any());
    }


    @Test
    @DisplayName("존재하지 않는 밥일기를 삭제하면 정해진 예외를 반환한다")
    public void delete_fail_mealDiaryNotFound(){
        //given
        when(mealDiaryRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> mealDiaryService.delete(1L,1L))
                .isInstanceOf(MealDiaryHandler.class)
                .hasFieldOrPropertyWithValue("code", MEALDIARY_NOT_FOUND);
    }


    @Test
    @DisplayName("밥일기의 제작자와 삭제를 요청하는 회원이 일치하지 않으면 정해진 예외를 반환한다")
    public void delete_fail_authenticate(){
        //given
        UserEntity spyMember = spy(testMember);
        UserEntity errUser = UserEntity.builder()
                .id(2L)
                .providerId("err")
                .followingCount(0)
                .followerCount(0)
                .build();
        UserEntity spyMember2 = spy(errUser);

        MealDiary testMealDiary = MealDiary.builder()
                .id(1L)
                .foodCategory(FoodCategory.FAST_FOOD)
                .location("test_location")
                .description("test_description")
                .isRevisit(IsRevisit.GOOD)
                .isReport(false)
                .likesCount(0)
                .commentCount(0)
                .userEntity(spyMember)
                .mealDiaryImages(new ArrayList<>())
                .mealDiaryKeywords(new ArrayList<>())
                .build();

        MealDiary spyMealDiary = spy(testMealDiary);
        MealDiaryImage testMealDiaryImage = MealDiaryImage.builder()
                .id(1L)
                .imageLink("test_mealDiary_image_link")
                .mealDiary(spyMealDiary)
                .build();

        spyMealDiary.getMealDiaryImages().add(testMealDiaryImage);

        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(spyMember2));
        when(mealDiaryRepository.findById(anyLong())).thenReturn(Optional.of(spyMealDiary));

        //when & then
        assertThatThrownBy(() -> mealDiaryService.delete(2L,1L))
                .isInstanceOf(UserEntityHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_AUTHENTICATE_FAILED);
    }


    @Test
    @DisplayName("밥일기 이미지 삭제 중 오류가 발생하면 정해진 예외를 반환한다")
    public void delete_fail_imageRemoveError() throws FileNotFoundException {
        //given
        UserEntity spyMember = spy(testMember);

        MealDiary testMealDiary = MealDiary.builder()
                .foodCategory(FoodCategory.FAST_FOOD)
                .location("test_location")
                .description("test_description")
                .isRevisit(IsRevisit.GOOD)
                .isReport(false)
                .likesCount(0)
                .commentCount(0)
                .userEntity(spyMember)
                .mealDiaryImages(new ArrayList<>())
                .mealDiaryKeywords(new ArrayList<>())
                .build();

        MealDiary spyMealDiary = spy(testMealDiary);
        MealDiaryImage testMealDiaryImage = MealDiaryImage.builder()
                .id(1L)
                .imageLink("test_mealDiary_image_link")
                .mealDiary(spyMealDiary)
                .build();

        spyMealDiary.getMealDiaryImages().add(testMealDiaryImage);

        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(spyMember));
        when(mealDiaryRepository.findById(anyLong())).thenReturn(Optional.of(spyMealDiary));
        doThrow(new ImageHandler(IMAGE_REMOVE_ERROR)).when(mealDiaryImageUtil).deleteImage(any());

        //when & then
        assertThatThrownBy(() -> mealDiaryService.delete(1L,1L))
                .isInstanceOf(ImageHandler.class)
                .hasFieldOrPropertyWithValue("code", IMAGE_REMOVE_ERROR);
    }


    @Test
    @DisplayName("밥일기에 삭제할 데이터를 찾지 못하면 정해진 예외를 반환한다")
    public void delete_fail_imageNotFound() throws FileNotFoundException {
        //given
        UserEntity spyMember = spy(testMember);

        MealDiary testMealDiary = MealDiary.builder()
                .foodCategory(FoodCategory.FAST_FOOD)
                .location("test_location")
                .description("test_description")
                .isRevisit(IsRevisit.GOOD)
                .isReport(false)
                .likesCount(0)
                .commentCount(0)
                .userEntity(spyMember)
                .mealDiaryImages(new ArrayList<>())
                .mealDiaryKeywords(new ArrayList<>())
                .build();

        MealDiary spyMealDiary = spy(testMealDiary);
        MealDiaryImage testMealDiaryImage = MealDiaryImage.builder()
                .id(1L)
                .imageLink("test_mealDiary_image_link")
                .mealDiary(spyMealDiary)
                .build();

        spyMealDiary.getMealDiaryImages().add(testMealDiaryImage);

        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(spyMember));
        when(mealDiaryRepository.findById(anyLong())).thenReturn(Optional.of(spyMealDiary));
        doThrow(new ImageHandler(IMAGE_NOT_FOUND)).when(mealDiaryImageUtil).deleteImage(any());

        //when & then
        assertThatThrownBy(() -> mealDiaryService.delete(1L,1L))
                .isInstanceOf(ImageHandler.class)
                .hasFieldOrPropertyWithValue("code", IMAGE_NOT_FOUND);
    }


    @Test
    @DisplayName("report()를 이용해서 게시글을 신고 할 수 있다")
    public void report_success(){
        //given
        MealDiary testMealDiary = MealDiary.builder()
                .id(1L)
                .foodCategory(FoodCategory.FAST_FOOD)
                .location("test_location")
                .description("test_description")
                .isRevisit(IsRevisit.GOOD)
                .isReport(false)
                .likesCount(0)
                .commentCount(0)
                .userEntity(testMember)
                .mealDiaryImages(new ArrayList<>())
                .mealDiaryKeywords(new ArrayList<>())
                .build();

        MealDiary spyMealDiary = spy(testMealDiary);

        MealDiaryReportDTO.MealDiaryReportRequestDTO request = MealDiaryReportDTO.MealDiaryReportRequestDTO.builder()
                .userID(1L)
                .mealDiaryId(1L)
                .reportReason(ReportReason.COMMERCIAL_ADV)
                .build();

        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(testMember));
        when(mealDiaryRepository.findById(anyLong())).thenReturn(Optional.of(spyMealDiary));
        when(mealDiaryReportRepository.existsByUserEntityAndMealDiary(any(),any())).thenReturn(false);

        //when
        MealDiaryReportDTO.MealDiaryReportResponseDTO response = mealDiaryService.report(request);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getMealDiaryId()).isEqualTo(1L);
        assertThat(spyMealDiary.isReport()).isTrue();

        verify(mealDiaryReportRepository, times(1)).save(any());
    }


    @Test
    @DisplayName("존재하지 않는 회원이 삭제 요청을 보내면 정해진 예외를 반환한다")
    public void report_memberNotFound(){
        //given
        MealDiaryReportDTO.MealDiaryReportRequestDTO request = MealDiaryReportDTO.MealDiaryReportRequestDTO.builder()
                .userID(1L)
                .mealDiaryId(1L)
                .reportReason(ReportReason.COMMERCIAL_ADV)
                .build();

        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> mealDiaryService.report(request))
                .isInstanceOf(UserEntityHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_NOT_FOUND);
    }


    @Test
    @DisplayName("존재하지 않는 게시글을 신고하려 하면 정해진 예외를 반환한다")
    public void report_mealDiaryNotFound(){
        //given
        MealDiaryReportDTO.MealDiaryReportRequestDTO request = MealDiaryReportDTO.MealDiaryReportRequestDTO.builder()
                .userID(1L)
                .mealDiaryId(1L)
                .reportReason(ReportReason.COMMERCIAL_ADV)
                .build();

        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(testMember));
        when(mealDiaryRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> mealDiaryService.report(request))
                .isInstanceOf(MealDiaryHandler.class)
                .hasFieldOrPropertyWithValue("code", MEALDIARY_NOT_FOUND);
    }


    @Test
    @DisplayName("이미 신고가 들어가있는 게시글은 신고 할 수 없다")
    public void report_fail_exist(){
        //given
        MealDiary testMealDiary = MealDiary.builder()
                .id(1L)
                .foodCategory(FoodCategory.FAST_FOOD)
                .location("test_location")
                .description("test_description")
                .isRevisit(IsRevisit.GOOD)
                .isReport(false)
                .likesCount(0)
                .commentCount(0)
                .userEntity(testMember)
                .mealDiaryImages(new ArrayList<>())
                .mealDiaryKeywords(new ArrayList<>())
                .build();

        MealDiary spyMealDiary = spy(testMealDiary);

        MealDiaryReportDTO.MealDiaryReportRequestDTO request = MealDiaryReportDTO.MealDiaryReportRequestDTO.builder()
                .userID(1L)
                .mealDiaryId(1L)
                .reportReason(ReportReason.COMMERCIAL_ADV)
                .build();

        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(testMember));
        when(mealDiaryRepository.findById(anyLong())).thenReturn(Optional.of(spyMealDiary));
        when(mealDiaryReportRepository.existsByUserEntityAndMealDiary(any(),any())).thenReturn(true);

        //when & then
        assertThatThrownBy(() -> mealDiaryService.report(request))
                .isInstanceOf(MealDiaryHandler.class)
                .hasFieldOrPropertyWithValue("code", MEALDIARY_REPORTED);
    }


    @Test
    @DisplayName("getReport()를 통해서 신고된 게시글을 조회 할 수 있다")
    public void getReport_success(){
        //given
        MealDiary testMealDiary = MealDiary.builder()
                .id(1L)
                .foodCategory(FoodCategory.FAST_FOOD)
                .location("test_location")
                .description("test_description")
                .isRevisit(IsRevisit.GOOD)
                .isReport(false)
                .likesCount(0)
                .commentCount(0)
                .userEntity(testMember)
                .mealDiaryImages(new ArrayList<>())
                .mealDiaryKeywords(new ArrayList<>())
                .build();
        MealDiary spyMealDiary = spy(testMealDiary);

        MealDiaryReport testMealDiaryReport = MealDiaryReport.builder()
                .id(1L)
                .reportReason(ReportReason.COMMERCIAL_ADV)
                .mealDiary(spyMealDiary)
                .userEntity(testMember)
                .build();

        when(mealDiaryReportRepository.findAll()).thenReturn(List.of(testMealDiaryReport));

        //when
        List<MealDiaryReportDTO.MealDiaryReportResponseDTO> response = mealDiaryService.getReport();

        //then
        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(1);
        assertThat(response.get(0).getMealDiaryId()).isEqualTo(1L);
    }


    // FIXME
    @Test
    @DisplayName("update()를 이용해서 밥일기의 정보를 업데이트 할 수 있다")
    public void update_success() {
        // given
        UserEntity spyMember = spy(testMember);

        MealDiary realMealDiary = MealDiary.builder()
                .foodCategory(FoodCategory.FAST_FOOD)
                .location("test_location")
                .description("test_description")
                .isRevisit(IsRevisit.GOOD)
                .isReport(false)
                .likesCount(0)
                .commentCount(0)
                .userEntity(spyMember)
                .mealDiaryImages(null)
                .mealDiaryKeywords(new ArrayList<>())
                .build();
        MealDiary testMealDiary = spy(realMealDiary);

        Keyword testKeyword1 = Keyword.builder()
                .id(1L)
                .keyword("한식")
                .build();

        Keyword testKeyword2 = Keyword.builder()
                .id(2L)
                .keyword("중식")
                .build();

        MealDiaryKeyword testMealDiaryKeyword = MealDiaryKeyword.builder()
                .keyword(testKeyword1)
                .mealDiary(testMealDiary)
                .build();

        MealDiaryKeyword testMealDiaryKeyword2 = MealDiaryKeyword.builder()
                .keyword(testKeyword2)
                .mealDiary(testMealDiary)
                .build();

        List<MealDiaryKeyword> mealDiaryKeywords = new ArrayList<>();
        mealDiaryKeywords.add(testMealDiaryKeyword);
        mealDiaryKeywords.add(testMealDiaryKeyword2);

        // DTO 생성
        MealDiaryUpdateDTO.MealDiaryUpdateRequestDTO request = MealDiaryUpdateDTO.MealDiaryUpdateRequestDTO.builder()
                .memberId(1L)
                .mealDiaryId(1L)
                .foodCategory(FoodCategory.FAST_FOOD)
                .keyword("업데,이트")
                .location("updated_location")
                .description("updated_description")
                .revisit(IsRevisit.NOT_GOOD)
                .build();

        // when
        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(spyMember));
        when(mealDiaryRepository.findById(anyLong())).thenReturn(Optional.of(testMealDiary));
        doNothing().when(mealDiaryKeywordRepository).deleteAllByMealDiary(any());

        // 서비스 호출
        MealDiaryUpdateDTO.MealDiaryUpdateResponseDTO response = mealDiaryService.update(request);

        // 업데이트된 MealDiary ID 확인
        System.out.println("Updated Meal Diary ID: " + response.getMealDiaryId());

        // then
        assertThat(response).isNotNull();
        /*assertThat(response.getMealDiaryId()).isEqualTo(1L); // 업데이트된 mealDiaryId 확인*/
    }


}