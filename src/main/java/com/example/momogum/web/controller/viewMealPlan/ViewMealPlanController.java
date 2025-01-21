package com.example.momogum.web.controller.viewMealPlan;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.service.viewMealDiaryService.ViewMealDiaryService;
import com.example.momogum.web.dto.viewMealDiary.ViewMealDiaryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mainpage")
@Tag(name = "메인 페이지 조회 로직")
public class ViewMealPlanController {

    private final ViewMealDiaryService viewMealDiaryService;

    @Operation(summary = "메인 페이지 또 올래요 시 조회",
            description = "메인 페이지에서 또 올래요를 눌렀을 때 ")
    @GetMapping("/revisit")
    public ApiResponse<ViewMealDiaryDTO.ViewMealDiaryResponseListDTO> getMealDiaryIsRevisit(@RequestParam Long userId) {

        ViewMealDiaryDTO.ViewMealDiaryResponseListDTO response = viewMealDiaryService.getMealDiaryIsRevisitedByLikesCount(userId);

        return ApiResponse.onSuccess(response);

    }
}
