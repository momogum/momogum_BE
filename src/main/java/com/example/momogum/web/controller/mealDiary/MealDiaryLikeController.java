package com.example.momogum.web.controller.mealDiary;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.service.mealDiaryService.MealDiaryLikeService;
import com.example.momogum.service.mealDiaryService.MealDiaryLikeServiceImpl;
import com.example.momogum.web.dto.mealDiary.MealDiaryLikeDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/meal-diaries/likes")
@Tag(name = "밥일기 좋아요 API")
@RequiredArgsConstructor
public class MealDiaryLikeController {

    private final MealDiaryLikeService mealDiaryLikeService;


    // 좋아요 토글형식으로 API 구현
    @Operation(summary = "밥일기 좋아요 토글 API")
    @PostMapping("/userId/{userId}/mealDiaryId/{mealDiaryId}")
    public ApiResponse<String> toggle(
            @PathVariable Long userId,
            @PathVariable Long mealDiaryId
    ) throws IOException {

        mealDiaryLikeService.toggle(userId,mealDiaryId);

        return ApiResponse.onSuccess("반영 되었습니다");
    }

    // 좋아요하는 회원 조회 API
    @Operation(summary = "좋아요 회원 조회 API", description = "좋아요를 등록한 회원을 조회합니다")
    @GetMapping("")
    public ApiResponse<List<MealDiaryLikeDTO.MealDiaryLikeResponseDTO>> get(
            @RequestParam Long mealDiaryId
    ){

        List<MealDiaryLikeDTO.MealDiaryLikeResponseDTO> result = mealDiaryLikeService.get(mealDiaryId);
        return ApiResponse.onSuccess(result);
    }
}
