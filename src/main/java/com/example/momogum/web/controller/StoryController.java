package com.example.momogum.web.controller;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.web.dto.MealDairyDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/meal-stories")
@Tag(name = "밥일기 관련 API")
public class StoryController {


    /**
     * 스토리 조회 (내가 확인한게 스토리 조회가 맞는지 모르겠음)
     *
     *  1. 자신이 팔로우하고 있는 회원들의 스토리를 조회
     *  2. 최신순으로 조회
     *  3. 조회 여부를 남길 수 있는 필드 필요
     *  4. 기획에서 기억나는 부분으로는 3일동안 조회 가능한걸로 기억함
     *      3일이 지난 후에는 스토리 보관함으로 이동하고
     *      여기서 하이라이트를 만들 수 있음
     *
     *  - 인스타 스토리같이 조회되는 기능이라면 읽지 않은 스토리를 우선적으로 조회하고,
     *      그 다음 조회하지 않은 스토리를 조회해야함
     * */
    @Operation(summary = "팔로우한 회원들의 밥일기 조회 API")
    @GetMapping("/memberId/{memberId}")
    public ApiResponse<List<MealDairyDTO.GetStoryFollowResponseDTO>> getFollowStories(
            @Parameter(name = "memberId", description = "추후 토큰으로 변경 될 수 있습니다")
            @PathVariable Long memberId) {

        MealDairyDTO.GetStoryFollowResponseDTO tempResult = MealDairyDTO.GetStoryFollowResponseDTO.builder()
                .memberImagePath("temp")
                .isRead(Boolean.FALSE)
                .nickname("temp")
                .build();

        List<MealDairyDTO.GetStoryFollowResponseDTO> resultList = List.of(tempResult);
        return ApiResponse.onSuccess(resultList);
    }


    /**
     * 개별 스토리 조회
     * */
    @Operation(summary = "개별 스토리 조회 API")
    @GetMapping("/storyId/{storyId}")
    public ApiResponse<MealDairyDTO.GetStoryResponseDTO> getOne(@PathVariable Long storyId) {

        return ApiResponse.onSuccess(MealDairyDTO.GetStoryResponseDTO.builder()
                .score(1)
                .foodCategory("temp")
                .keyword("temp")
                .location("temp")
                .review("temp")
                .imagePath("temp")
                .build());
    }

}
