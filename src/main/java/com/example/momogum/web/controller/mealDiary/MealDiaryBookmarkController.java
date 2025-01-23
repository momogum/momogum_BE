package com.example.momogum.web.controller.mealDiary;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.service.mealDiaryService.MealDiaryBookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

}
