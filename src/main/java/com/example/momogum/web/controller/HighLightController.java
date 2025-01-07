package com.example.momogum.web.controller;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.web.dto.HighLightDTO;
import com.example.momogum.web.dto.StoryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/highLight")
@Tag(name = "스토리 하이라이트 관련 API")
public class HighLightController {


    /**
     * 스토리 하이라이트 생성
     *
     *  0. 하이라이트에 넣을 스토리 넣기
     *  1. 커버 이미지 생성
     *  2. 하이라이트 이름
     *
     * */
    @Operation(summary = "스토리 하이라이트 생성 API")
    @PostMapping(path = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<HighLightDTO.CreateHighLightResponseDTO> create(
            @Parameter(description = "스토리 하이라이트의 배경화면으로, 사용되는 이미지 파일입니다.")
            @RequestPart(value = "file") MultipartFile multipartFile,
            @Parameter(description = "기술명세서 하단의 스키마를 확인해주세요")
            @RequestBody HighLightDTO.CreateHighLightRequestDTO createRequestDTO) {

        return ApiResponse.onSuccess(HighLightDTO.CreateHighLightResponseDTO.builder()
                // API 구현시에는 수정될 예정입니다 FIXME
                .highLightId(1L)
                .build());
    }





    /**
     * 보관중인 스토리 하이라이트에 추가 (와이어프레임 상에 없는 기능) -> 토글식으로 구현하는 것도 방법이려나
     *
     *  1. 스토리 식별자를 통해서 하이라이트에 추가
     *  2. 스토리에 매핑된 하이라이트 아이디를 업데이트 하는 식으로 하면 될듯??
     *
     * */
    @Operation(summary = "하이라이트 스토리 추가 API")
    @PostMapping("/highLightId/{highLightId}")
    public ApiResponse<HighLightDTO.CreateHighLightResponseDTO> addHighLight(
            @Parameter(name = "storyId", description = "하이라이트에 추가할 스토리를 입력해주세요")
            @RequestBody List<Long> storyId,
            @Parameter(name = "highLightId", description = "추가 할 하이라이트 ID를 입력해주세요")
            @PathVariable Long highLightId){

        return ApiResponse.onSuccess(HighLightDTO.CreateHighLightResponseDTO.builder()
                // API 구현시에는 수정될 예정입니다 FIXME
                .highLightId(1L)
                .build());
    }



    // 스토리 수정기능이 필요하려나?


    /**
     * 하이라이트 조회
     * */
    @Operation(summary = "하이라이트 조회 API")
    @GetMapping("/highLightId/{highLightId}")
    public ApiResponse<HighLightDTO.GetHighLightResponseDTO> getHighLight(
            @Parameter(name = "highLightId", description = "조회 할 하이라이트 ID를 입력해주세요")
            @PathVariable Long highLightId){

        String imagePath = "temp";
        List<String> imagePaths = List.of(imagePath);

        return ApiResponse.onSuccess(HighLightDTO.GetHighLightResponseDTO
                .builder()
                .imagePaths(imagePaths)
                .build());
    }

    /**
     * 하이라이트 삭제
     *
     * 이건 바로 삭제로 할지 며칠간 보관하고 복구가 가능하도록 구현할지
     *
     * 일단 바로 삭제로 생각하고 구현
     * */
    @Operation(summary = "하이라이트 삭제 API",
            description = "삭제 요청 후 3일 이후에 삭제됩니다")
    @PatchMapping("/highLightId/{highLightId}")
    public ApiResponse<String> deleteHighLight(
            @Parameter(name = "highLightId", description = "삭제 할 하이라이트 ID를 입력해주세요")
            @PathVariable Long highLightId){

        return ApiResponse.onSuccess("하이라이트가 삭제되었습니다");
    }
}
