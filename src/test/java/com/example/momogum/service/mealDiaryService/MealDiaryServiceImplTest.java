package com.example.momogum.service.mealDiaryService;

import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.common.enums.FoodCategory;
import com.example.momogum.domain.common.enums.IsRevisit;
import com.example.momogum.repository.mealDiaryBookmarkRepo.MealDiaryBookmarkRepository;
import com.example.momogum.repository.mealDiaryCommentsRepo.MealDiaryCommentsRepository;
import com.example.momogum.repository.mealDiaryRepo.*;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.mealDiary.MealDairiesDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

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

    MealDiary testMealDiary;


    @BeforeEach
    void setUp() {
        testMember = UserEntity.builder()
                .id(1L)
                .phoneNumber("test")
                .name("test")
                .nickname("test")
                .about("test")
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
        spy(testMealDiary);

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

        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(testMember));
        when(mealDiaryRepository.save(any())).thenReturn(testMealDiary);
        when(testMealDiary.getId()).thenReturn(1L);

        //when
        MealDairiesDTO.CreateStoryResponseDTO response = mealDiaryService.save(request, files);
        System.out.println(response.getMealDiaryId());

        //then
        assertThat(response).isNotNull();
        assertThat(response.getMealDiaryId()).isEqualTo(1L);

        verify(mealDiaryStoryRepository,times(1)).save(any());
    }
}