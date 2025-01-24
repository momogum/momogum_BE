package com.example.momogum.web.controller.mealDiary;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.service.mealDiaryService.MealDiaryService;
import com.example.momogum.web.dto.mealDiary.MealDairiesDTO;
import com.example.momogum.web.dto.mealDiary.MealDiaryReportDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/meal-diaries")
@Tag(name = "밥일기 API")
@RequiredArgsConstructor
public class MealDiaryController {

    private final MealDiaryService mealDiaryService;

    @Operation(
            summary = "밥일기 생성 API",
            description = "사진 넣으실때 반드시 **키 이름을 value** 에 맞춰주세요. <br>" +
                    "또한 사진이 아닌 request 정보를 입력할때는 반드시 **Content-Type: application/json** 으로 설정해주셔야 정상적으로 작동합니다"
    )
    @PostMapping(path = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<MealDairiesDTO.CreateStoryResponseDTO> create(
            @RequestPart(value = "files") List<MultipartFile> files,
            @RequestPart(value = "request") MealDairiesDTO.CreateStoryRequestDTO request) {

        MealDairiesDTO.CreateStoryResponseDTO result = mealDiaryService.save(request, files);

        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "단일 밥일기 조회 API")
    @GetMapping("")
    public ApiResponse<MealDairiesDTO.GetMealDiaryResponseDTO> get(@RequestParam Long mealDairyId,
                                                                   @RequestParam Long userId){
        MealDairiesDTO.GetMealDiaryResponseDTO getMealDiaryResponseDTO = mealDiaryService.get(mealDairyId,userId);

        return ApiResponse.onSuccess(getMealDiaryResponseDTO);
    }


    @Operation(summary = "회원 보관 밥일기 API", description = "회원이 작성한 모든 밥일기를 조회합니다")
    @GetMapping("/memberId/{memberId}/all")
    public ApiResponse<List<MealDairiesDTO.GetAllMealDiaryResponseDTO>> getMemberMealDiaries(
            @PathVariable Long memberId) {

        List<MealDairiesDTO.GetAllMealDiaryResponseDTO> result = mealDiaryService.getAll(memberId);
        return ApiResponse.onSuccess(result);

    }

    @Operation(summary = "밥일기 삭제 API")
    @DeleteMapping("/mealDiaryId/{mealDiaryId}/userId/{userId}")
    public ApiResponse<String> delete(
            @PathVariable Long userId,
            @PathVariable Long mealDiaryId) throws FileNotFoundException {

        mealDiaryService.delete(userId, mealDiaryId);

        return ApiResponse.onSuccess("밥일기 삭제되었습니다");
    }

    @Operation(summary = "신고하기 API")
    @PostMapping("/report")
    public ApiResponse<MealDiaryReportDTO.MealDiaryReportResponseDTO> report(@RequestBody MealDiaryReportDTO.MealDiaryReportRequestDTO request){

        MealDiaryReportDTO.MealDiaryReportResponseDTO result = mealDiaryService.report(request);

        return ApiResponse.onSuccess(result);
    }
}
