package com.example.momogum.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TestDTO {

    @Getter
    public static class TestRequestDTO{

        @Schema(description = "DTO에 대한 설명을 추가합니다")
        String name;

        @Schema(description = "DTO에 대한 설명을 추가합니다")
        String password;

    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class TestResponseDTO{

        @Schema(description = "DTO에 대한 설명을 추가합니다")
        String name;

        @Schema(description = "DTO에 대한 설명을 추가합니다")
        String password;

    }
}
