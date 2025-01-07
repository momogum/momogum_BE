package com.example.momogum.web.controller;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.web.dto.HighLightDTO;
import com.example.momogum.web.dto.StoryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ApiResponse<HighLightDTO.CreateResponseDTO> create(
            @Parameter(description = "스토리 하이라이트의 배경화면으로, 사용되는 이미지 파일입니다.")
            @RequestPart(value = "file") MultipartFile multipartFile,
            @Parameter(description = "기술명세서 하단의 스키마를 확인해주세요")
            @RequestBody HighLightDTO.CreateRequestDTO createRequestDTO) {

        return ApiResponse.onSuccess(HighLightDTO.CreateResponseDTO.builder()
                // API 구현시에는 수정될 예정입니다 FIXME
                .id(1L)
                .build());
    }





    /**
     * 보관중인 스토리 하이라이트에 추가 (와이어프레임 상에 없는 기능) -> 토글식으로 구현하는 것도 방법이려나
     *
     *  1. 스토리 식별자를 통해서 하이라이트에 추가
     *
     * */



    // 스토리 수정기능이 필요하려나?



    /**
     * 스토리 삭제
     *
     * 이건 바로 삭제로 할지 며칠간 보관하고 복구가 가능하도록 구현할지
     *
     * 일단 바로 삭제로 생각하고 구현
     * */
}
