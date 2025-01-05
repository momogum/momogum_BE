package com.example.momogum.web.controller;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.web.dto.TestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.hibernate.boot.model.process.spi.MetadataBuildingProcess.build;

@RestController
@RequestMapping("/test")
@Tag(name = "기술명세서 예시 템플릿 API")
public class TestController {

    @GetMapping("")
    @Operation(summary = "API에 대한 간단한 설명을 추가합니다",
            description = "API에 대한 상세한 설명을 추가합니다")
    public ApiResponse<TestDTO.TestResponseDTO> test(@RequestBody TestDTO.TestRequestDTO request) {
        return ApiResponse.onSuccess(
                TestDTO.TestResponseDTO.builder()
                                .name(request.getName())
                                .password(request.getPassword())
                                .build());
    }
}
