package com.example.momogum.web.controller;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.service.MealDiaryImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members/images")
@Slf4j
@Tag(name = "개인 프로필 이미지 관리 API")
public class MealDiaryImageController {


    private final MealDiaryImageService mealDiaryImageService;


    @Operation(summary = "프로필 사진 등록 API" , description = "Authorization 헤더에 토큰을 넣어주세요")
    @PostMapping(path = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> uploadMemberProfileImage(
            @RequestPart(value = "file") MultipartFile multipartFile,
            @RequestParam Long mealDiaryId
    ) throws IOException {

        String dirName = "meal_diary_images";
        String url = mealDiaryImageService.upload(multipartFile, dirName, mealDiaryId);
        log.info("파일 업로드 완료: {}", url);

        return ApiResponse.onSuccess(url);
    }





    @Operation(summary = "본인 프로필 사진 조회 API" , description = "Authorization 헤더에 토큰을 넣어주세요")
    @GetMapping(path = "")
    public ResponseEntity<byte[]> getMemberProfileImage(
            @RequestParam Long mealDiaryId) {

        try {
            byte[] fileData = mealDiaryImageService.download(mealDiaryId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "filename");
            log.info("파일 다운로드 완료: ID {}", mealDiaryId);

            return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
        } catch (Exception e) {

            log.error("파일 다운로드 오류: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }




    @Operation(summary = "프로필 사진 조회 API" , description = "Authorization 헤더에 토큰을 넣어주세요")
    @GetMapping(path = "/{mealDiaryId}")
    public ResponseEntity<byte[]> getProfileImage(
            @PathVariable Long mealDiaryId) {


        try {
            byte[] fileData = mealDiaryImageService.downloadImage(mealDiaryId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "filename");
            log.info("파일 다운로드 완료: ID {}", mealDiaryId);

            return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
        } catch (Exception e) {

            log.error("파일 다운로드 오류: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }






    @Operation(summary = "프로필 사진 삭제 API" , description = "Authorization 헤더에 토큰을 넣어주세요")
    @DeleteMapping(path = "")
    public ApiResponse<String> deleteMemberProfileImage(
            @RequestParam Long mealDiaryId) {


        try {

            mealDiaryImageService.deleteFile(mealDiaryId);

            return ApiResponse.onSuccess("삭제에 성공하였습니다");

        } catch (Exception e) {
            log.error("파일 삭제 오류: {}", e.getMessage());
            return ApiResponse.onFailure("404","이미지를 찾을 수 없습니다",null);
        }
    }






    @Operation(summary = "프로필 사진 업데이트 API" , description = "Authorization 헤더에 토큰을 넣어주세요")
    @PutMapping(path = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> updateMemberProfileImage(
            @RequestPart(value = "file") MultipartFile multipartFile,
            @RequestParam Long mealDiaryId
    ) throws IOException {

        String dirName = "meal_diary_images";


        try {

            String url = mealDiaryImageService.updateProfileImage(multipartFile, dirName, mealDiaryId);
            log.info("프로필 이미지 업데이트 완료: {}", url);

            return ApiResponse.onSuccess(url);

        } catch (Exception e) {

            log.error("프로필 이미지 업데이트 오류: {}", e.getMessage());

            return ApiResponse.onFailure("404","이미지를 찾을 수 없습니다",null);
        }
    }
}
