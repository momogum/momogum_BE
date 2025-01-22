package com.example.momogum.web.controller.mealDiary;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.service.mealDiaryService.MealDiaryLikeService;
import com.example.momogum.service.mealDiaryService.MealDiaryLikeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meal-diaries/likes")
@Tag(name = "밥일기 좋아요 API")
@RequiredArgsConstructor
public class MealDiaryLikeController {

    private final MealDiaryLikeService mealDiaryLikeService;


    // 좋아요 토글형식으로 API 구현
    @Operation(summary = "밥일기 좋아요 토글 API")
    @GetMapping("/userId/{userId}/mealDiaryId/{mealDiaryId}")
    public ApiResponse<String> toggle(
            @PathVariable Long userId,
            @PathVariable Long mealDiaryId
    ){

        mealDiaryLikeService.toggle(userId,mealDiaryId);

        return ApiResponse.onSuccess("반영 되었습니다");
    }

    // 좋아요하는 회원 조회 API
}
