package com.example.momogum.web.controller;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.service.mealDiaryService.MealDiaryImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meal-diaries/images")
@Slf4j
@Tag(name = "밥일기 이미지 API")
public class MealDiaryImageController {


    private final MealDiaryImageService mealDiaryImageService;


    //이미지 파일들 s3에 저장 후 테이블 추가
    @Operation(summary = "이미지 저장 API", description = "저장해야하는 이미지 파일과 밥일기ID를 넣어주세요")
    @PostMapping(value = "/mealDiaryId/{mealDairyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<List<String>> addImages(@RequestPart List<MultipartFile> files,
                                               @PathVariable Long mealDairyId) {

        String dirName = "meal_diary_images";
        List<String> result = mealDiaryImageService.uploadImages(files, dirName, mealDairyId);

        return ApiResponse.onSuccess(result);
    }

    // 이미지 삭제
    @Operation(summary = "이미지 삭제 API", description = "삭제해야하는 밥일기ID를 넣어주세요")
    @DeleteMapping("/mealDiaryId/{mealDairyId}")
    public ApiResponse<String> deleteImage(@PathVariable Long mealDairyId) throws FileNotFoundException {

        mealDiaryImageService.deleteImage(mealDairyId);
        return ApiResponse.onSuccess("Image deleted successfully");
    }

    // 이미지 조회
    @Operation(summary = "단일 이미지 조회 API", description = "조회해야하는 이미지 filename을 넣어주세요")
    @GetMapping("/")
    public ApiResponse<String> getImageByFileName(
            @RequestParam String fileName) {

        String result = mealDiaryImageService.findImageByFileName(fileName);

        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "이미지 조회 API", description = "조회 해야하는 밥일기ID를 넣어주세요")
    @GetMapping("/mealDiaryId/{mealDairyId}")
    public ApiResponse<List<String>> getImagesByMealDiaryId(
            @PathVariable Long mealDairyId) {

        List<String> result = mealDiaryImageService.findImagesByMealId(mealDairyId);

        return ApiResponse.onSuccess(result);
    }
}
