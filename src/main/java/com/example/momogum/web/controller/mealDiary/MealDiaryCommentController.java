package com.example.momogum.web.controller.mealDiary;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.security.CustomUserDetails;
import com.example.momogum.service.mealDiaryService.MealDiaryCommentService;
import com.example.momogum.web.dto.mealDiary.MealDiaryCommentCreateDTO;
import com.example.momogum.web.dto.mealDiary.MealDiaryCommentDeleteDTO;
import com.example.momogum.web.dto.mealDiary.MealDiaryCommentUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/meal-diaries/comments")
@Tag(name = "밥일기 댓글 API")
@RequiredArgsConstructor
public class MealDiaryCommentController {

    private final MealDiaryCommentService mealDiaryCommentService;

    // 댓글 생성
    @Operation(summary = "밥일기 댓글 생성 API")
    @PostMapping("")
    public ApiResponse<MealDiaryCommentCreateDTO.MealDiaryCommentResponseDTO> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MealDiaryCommentCreateDTO.MealDiaryCommentRequestDTO request) throws IOException {

        Long userId = userDetails.getId();


        MealDiaryCommentCreateDTO.MealDiaryCommentResponseDTO result = mealDiaryCommentService.create(userId, request);

        return ApiResponse.onSuccess(result);
    }

    // 댓글 수정
    @Operation(summary = "밥일기 댓글 수정 API")
    @PatchMapping("")
    public ApiResponse<MealDiaryCommentUpdateDTO.MealDiaryCommentUpdateResponseDTO> update(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MealDiaryCommentUpdateDTO.MealDiaryCommentUpdateRequestDTO request){

        Long userId = userDetails.getId();
        MealDiaryCommentUpdateDTO.MealDiaryCommentUpdateResponseDTO result = mealDiaryCommentService.update(userId, request);

        return ApiResponse.onSuccess(result);
    }

    // 댓글 삭제
    @Operation(summary = "밥일기 댓글 삭제 API")
    @DeleteMapping("")
    public ApiResponse<String> delete(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MealDiaryCommentDeleteDTO.MealDiaryCommentDeleteRequestDTO request){

        Long userId = userDetails.getId();
        mealDiaryCommentService.delete(userId, request);

        return ApiResponse.onSuccess("삭제되었습니다");
    }

    // 댓글 조회는 게시글 조회 로직에 포함되는게 좋을 것 같음
}
