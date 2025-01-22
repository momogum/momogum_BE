package com.example.momogum.web.controller;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.service.mealDiaryService.MealDiaryImageService;
import com.example.momogum.service.mealDiaryService.MealDiaryService;
import com.example.momogum.web.dto.MealDairiesDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/meal-diaries")
@Tag(name = "밥일기 관련 API")
@RequiredArgsConstructor
public class MealDiaryController {

    private final MealDiaryService mealDiaryService;

    @Operation(
            summary = "밥일기 생성 API",
            description = "사진 넣으실때 반드시 **키 이름을 value** 에 맞춰주세요. <br>" +
                    "또한 사진이 아닌 정보를 입력할때는 반드시 **Content-Type: application/json** 으로 설정해주셔야 정상적으로 작동합니다"
    )
    @PostMapping(path = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<MealDairiesDTO.CreateStoryResponseDTO> create(
            @RequestPart(value = "files") List<MultipartFile> files,
            @RequestPart(value = "request") MealDairiesDTO.CreateStoryRequestDTO request) {

        MealDairiesDTO.CreateStoryResponseDTO result = mealDiaryService.save(request, files);

        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "밥일기 조회 API")
    @GetMapping("")
    public ApiResponse<MealDairiesDTO.GetMealDiaryResponseDTO> get(@RequestParam Long mealDairyId){
        MealDairiesDTO.GetMealDiaryResponseDTO getMealDiaryResponseDTO = mealDiaryService.get(mealDairyId);

        return ApiResponse.onSuccess(getMealDiaryResponseDTO);
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
    public ApiResponse<List<MealDairiesDTO.GetStoryMemberStoryResponseDTO>> getMemberMealDiaries(
            @Parameter(name = "memberId", description = "추후 토큰으로 변경 될 수 있습니다")
            @PathVariable Long memberId) {

        String imagePath = "temp";
        List<String> imagePaths = List.of(imagePath);

        MealDairiesDTO.GetStoryMemberStoryResponseDTO tempResult = MealDairiesDTO.GetStoryMemberStoryResponseDTO
                .builder()
                .imagePaths(imagePaths)
                .build();

        List<MealDairiesDTO.GetStoryMemberStoryResponseDTO> resultList = List.of(tempResult);
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
