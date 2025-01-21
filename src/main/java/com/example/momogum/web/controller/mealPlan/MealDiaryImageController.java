package com.example.momogum.web.controller.mealPlan;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.service.MealDiaryImageService;
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
    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<List<String>> addImages(@RequestPart List<MultipartFile> files,
                                               @RequestParam Long mealDiaryId) {

        String dirName = "meal_diary_images";
        List<String> result = mealDiaryImageService.uploadImages(files, dirName, mealDiaryId);

        return ApiResponse.onSuccess(result);
    }

    // 이미지 삭제
    @DeleteMapping("/")
    public ApiResponse<String> deleteImage(@RequestParam Long mealDiaryId) throws FileNotFoundException {

        mealDiaryImageService.deleteImage(mealDiaryId);
        return ApiResponse.onSuccess("Image deleted successfully");
    }

    // 이미지 조회
    @GetMapping("/")
    public ApiResponse<String> getImageByFileName(
            @RequestParam String fileName) {

        String result = mealDiaryImageService.findImageByFileName(fileName);

        return ApiResponse.onSuccess(result);
    }

    @GetMapping("/all/{mealDairyId}")
    public ApiResponse<List<String>> getImagesByFeedId(
            @PathVariable Long mealDairyId) {

        List<String> result = mealDiaryImageService.findImagesByMealId(mealDairyId);

        return ApiResponse.onSuccess(result);
    }
}
