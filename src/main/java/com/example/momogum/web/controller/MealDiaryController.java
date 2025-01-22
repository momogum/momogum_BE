package com.example.momogum.web.controller;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.web.dto.MealDairyDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meal-diaries")
@Tag(name = "밥일기 관련 API")
public class MealDiaryController {



    /**
     * 밥일기 추가
     *
     *  1. 사진선택
     *  2. 식사메뉴 선택 ( 한식, 중식, 일식....근데 카테고리가 얼마나 어떻게 있는지 모르겠음 )
     *  3. #키워드 선택 ( 이것도 작동방식과 사용 관련해서 여쭤봐야할듯 )
     *  4. 정확한 메뉴 이름
     *  5. 식사 위치
     *  6. 간단한 식사 경험
     *  7. 또 오고 싶은 곳인가요? ( 밑의 항목들 중에 선택 )
     *      -> 재방문 의사 1번 정도 있어요
     *      -> 같은 메뉴를 또 먹어보고 싶어요
     *      -> 이곳의 다른 메뉴도 궁금해요
     *      -> 자주 오고 싶어요
     *      -> 다시 방문하고 싶지 않아요
     *  8. 전체 평점 ( 0.1점 씩 올라감, 5점만점)
     *
     *  밥일기 생성시, 스토리도 자동으로 업로드
     * */
    @Operation(summary = "밥일기 생성 API")
    @PostMapping(path = "")
    public ApiResponse<MealDairyDTO.CreateStoryResponseDTO> create(
            @Parameter(description = "기술명세서 하단의 스키마를 확인해주세요")
            @RequestBody MealDairyDTO.CreateStoryRequestDTO createRequestDTO) {

        return ApiResponse.onSuccess(MealDairyDTO.CreateStoryResponseDTO.builder()
                // API 구현시에는 수정될 예정입니다 FIXME
                .storyId(1L)
                .build());
    }

    /**
     * 자신의 보관 게시글 전부 조회
     *
     *  1. 회원정보를 받아서
     *  2. 그걸 가지고 회원에 매핑된 스토리 전부 조회
     *
     * */
    @Operation(summary = "회원 보관 밥일기 API")
    @GetMapping("/memberId/{memberId}/all")
    public ApiResponse<List<MealDairyDTO.GetStoryMemberStoryResponseDTO>> getMemberMealDiaries(
            @Parameter(name = "memberId", description = "추후 토큰으로 변경 될 수 있습니다")
            @PathVariable Long memberId) {

        String imagePath = "temp";
        List<String> imagePaths = List.of(imagePath);

        MealDairyDTO.GetStoryMemberStoryResponseDTO tempResult = MealDairyDTO.GetStoryMemberStoryResponseDTO
                .builder()
                .imagePaths(imagePaths)
                .build();

        List<MealDairyDTO.GetStoryMemberStoryResponseDTO> resultList = List.of(tempResult);
        return ApiResponse.onSuccess(resultList);
    }


    /**
     * 밥일기 삭제
     *
     * 이건 바로 삭제로 할지 며칠간 보관하고 복구가 가능하도록 구현할지
     *
     * 일단 바로 삭제로 생각하고 구현
     * */
    @Operation(summary = "스토리 삭제 API",
            description = "삭제 요청 후 3일 이후에 삭제됩니다")
    @PatchMapping("/mealDiaryId/{mealDiaryId}")
    public ApiResponse<String> deleteStory(
            @Parameter(name = "mealDiaryId", description = "삭제 할 밥일기 ID를 입력해주세요")
            @PathVariable Long mealDiaryId){

        return ApiResponse.onSuccess("밥일기 삭제되었습니다");
    }
}
