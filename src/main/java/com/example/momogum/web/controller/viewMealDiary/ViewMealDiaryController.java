package com.example.momogum.web.controller.viewMealDiary;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.domain.common.enums.FoodCategory;
import com.example.momogum.service.viewMealDiaryService.ViewMealDiaryService;
import com.example.momogum.web.dto.viewMealDiary.ViewMealDiaryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mainpage")
@Tag(name = "메인 페이지 조회 로직")
public class ViewMealDiaryController {

    private final ViewMealDiaryService viewMealDiaryService;

    @Operation(summary = "메인 페이지 또 올래요 시 조회",
            description = "메인 페이지에서 또 올래요를 눌렀을 때 사용될 API입니다. ")
    @GetMapping("/revisit")
    public ApiResponse<List<ViewMealDiaryDTO.MainViewMealDiaryResponse>> getMealDiaryIsRevisit(
            @RequestParam Long userId,
            @RequestParam Integer page) {

        List<ViewMealDiaryDTO.MainViewMealDiaryResponse> response = viewMealDiaryService.getMealDiaryIsRevisitedByLikesCount(userId, page);

        return ApiResponse.onSuccess(response);

    }

    @Operation(summary = "메인 페이지 음식 카테고리 조회",
            description = "메인 페이지에서 각 카테고리를 눌렀을 때 사용되는 API입니다.<br>" +
                    " 각 카테고리 별로 RequestParam에 넣어서 주시면 해당 값을 반환합니다.<br>" +
                    "KOREAN(한식), CHINESE(중식), JAPANESE(일식), WESTERN(양식)" +
                    ", ASIAN(아시안 푸드), FAST_FOOD(패스트 푸드), CAFE(카페), ETC(기타) 해당 태그 맞춰서 넣어 주세요")
    @GetMapping("/{foodCategory}")
    public ApiResponse<List<ViewMealDiaryDTO.MainViewMealDiaryResponse>> getMealDiaryByFoodCategory(
            @RequestParam Long userId,
            @RequestParam Integer page,
            @PathVariable FoodCategory foodCategory
            ) {

        List<ViewMealDiaryDTO.MainViewMealDiaryResponse> response = viewMealDiaryService.getMealDiaryByFoodCategory(userId,page ,foodCategory);

        return ApiResponse.onSuccess(response);

    }
}
