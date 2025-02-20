package com.example.momogum.web.controller.mealDiary;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.security.CustomUserDetails;
import com.example.momogum.service.mealDiaryService.MealDiaryService;
import com.example.momogum.web.dto.mealDiary.MealDairiesDTO;
import com.example.momogum.web.dto.mealDiary.MealDiaryReportDTO;
import com.example.momogum.web.dto.mealDiary.MealDiaryUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart(value = "request") MealDairiesDTO.CreateStoryRequestDTO request) {

        Long userId = userDetails.getId();

        MealDairiesDTO.CreateStoryResponseDTO result = mealDiaryService.save(userId, request, files);

        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "단일 밥일기 조회 API")
    @GetMapping("")
    public ApiResponse<MealDairiesDTO.GetMealDiaryResponseDTO> get(@RequestParam Long mealDairyId,
                                                                   @AuthenticationPrincipal CustomUserDetails userDetails){

        Long userId = userDetails.getId();
        MealDairiesDTO.GetMealDiaryResponseDTO getMealDiaryResponseDTO = mealDiaryService.get(mealDairyId,userId);

        return ApiResponse.onSuccess(getMealDiaryResponseDTO);
    }


    @Operation(summary = "회원 보관 밥일기 API", description = "회원이 작성한 모든 밥일기를 조회합니다")
    @GetMapping("/all")
    public ApiResponse<List<MealDairiesDTO.GetAllMealDiaryResponseDTO>> getMemberMealDiaries(
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        Long userId = userDetails.getId();
        List<MealDairiesDTO.GetAllMealDiaryResponseDTO> result = mealDiaryService.getAll(userId);
        return ApiResponse.onSuccess(result);

    }


    @Operation(summary = "밥일기 삭제 API")
    @DeleteMapping("/mealDiaryId/{mealDiaryId}")
    public ApiResponse<String> delete(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long mealDiaryId) throws FileNotFoundException {

        Long userId = userDetails.getId();
        mealDiaryService.delete(userId, mealDiaryId);

        return ApiResponse.onSuccess("밥일기 삭제되었습니다");
    }


    @Operation(summary = "신고하기 API")
    @PostMapping("/report")
    public ApiResponse<MealDiaryReportDTO.MealDiaryReportResponseDTO> report(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MealDiaryReportDTO.MealDiaryReportRequestDTO request){

        Long userId = userDetails.getId();
        MealDiaryReportDTO.MealDiaryReportResponseDTO result = mealDiaryService.report(userId, request);

        return ApiResponse.onSuccess(result);
    }


    // 로직만 구현해놓고 기획안 나오는대로 리턴 값 FIXME
    @Operation(summary = "신고 게시글 조회 API", description = "기획에는 없지만 필요한 기능이라 생각되어 추가했습니다")
    @GetMapping("/report")
    public ApiResponse<List<MealDiaryReportDTO.MealDiaryReportResponseDTO>> getReport(){

        List<MealDiaryReportDTO.MealDiaryReportResponseDTO> result = mealDiaryService.getReport();

        return ApiResponse.onSuccess(result);
    }


    @Operation(summary = "밥일기 수정 API",
    description = "키워드 필드를 제외하고는 모두 필드 업데이트 방식입니다 <br> " +
            "키워드 필드는 아예 삭제하고 새로운 키워드를 저장하는 방식입니다")
    @PatchMapping("/")
    public ApiResponse<MealDiaryUpdateDTO.MealDiaryUpdateResponseDTO> update(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MealDiaryUpdateDTO.MealDiaryUpdateRequestDTO request){

        Long userId = userDetails.getId();
        MealDiaryUpdateDTO.MealDiaryUpdateResponseDTO response = mealDiaryService.update(userId, request);

        return ApiResponse.onSuccess(response);
    }
}
