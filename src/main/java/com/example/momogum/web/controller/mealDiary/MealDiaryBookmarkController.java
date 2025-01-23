package com.example.momogum.web.controller.mealDiary;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.service.mealDiaryService.MealDiaryBookmarkService;
import com.example.momogum.web.dto.mealDiary.MealDairiesDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meal-diaries/bookmarks")
@Tag(name = "밥일기 북마크 API")
@RequiredArgsConstructor
public class MealDiaryBookmarkController {

    private final MealDiaryBookmarkService mealDiaryBookmarkService;

    // 북마크 토글
    @Operation(summary = "밥일기 북마크 토글 API")
    @PostMapping("/userId/{userId}/mealDiaryId/{mealDiaryId}")
    public ApiResponse<String> toggle(@PathVariable Long userId,
                                      @PathVariable Long mealDiaryId) {

        mealDiaryBookmarkService.toggle(userId,mealDiaryId);

        return ApiResponse.onSuccess("반영 되었습니다");
    }

    // 북마크한 게시글 조회하는 API
    // 조회가 어떻게 될지 모르겠어서, 일단 회원 프로필에서 조회할거라 생각하고 모든 게시글을 조회하는 것과 같은 데이터를 반환하도록 구현하였습니다 FIXME
    @Operation(summary = "북마크 게시글 조회 API", description = "북마크가 되어있는 게시글을 조회합니다")
    @GetMapping("")
    public ApiResponse<List<MealDairiesDTO.GetAllMealDiaryResponseDTO>> get(Long userId){

        List<MealDairiesDTO.GetAllMealDiaryResponseDTO> result = mealDiaryBookmarkService.get(userId);
        return ApiResponse.onSuccess(result);
    }

}
