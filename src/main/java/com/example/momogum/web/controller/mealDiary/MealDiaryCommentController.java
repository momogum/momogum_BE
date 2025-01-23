package com.example.momogum.web.controller.mealDiary;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.service.mealDiaryService.MealDiaryCommentService;
import com.example.momogum.web.dto.mealDiary.MealDiaryCommentDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meal-diaries/comments")
@Tag(name = "밥일기 댓글 API")
@RequiredArgsConstructor
public class MealDiaryCommentController {

    private final MealDiaryCommentService mealDiaryCommentService;

    // 댓글 생성
    @Operation(summary = "밥일기 댓글 생성 API")
    @PostMapping("")
    public ApiResponse<MealDiaryCommentDTO.MealDiaryCommentResponseDTO> create(
            @RequestBody MealDiaryCommentDTO.MealDiaryCommentRequestDTO request){

        MealDiaryCommentDTO.MealDiaryCommentResponseDTO result = mealDiaryCommentService.create(request);

        return ApiResponse.onSuccess(result);
    }

    // 댓글 수정

    // 댓글 삭제

    // 댓글 조회는 게시글 조회 로직에 포함되는게 좋을 것 같음
}
