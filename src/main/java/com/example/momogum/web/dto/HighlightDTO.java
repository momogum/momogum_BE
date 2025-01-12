package com.example.momogum.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class HighlightDTO {


    @Getter
    public static class CreateHighlightRequestDTO {

        @Schema(description = "하이라이트에 추가 할 스토리를 선택합니다")
        List<Long> storyId;

        @Schema(description = "하이라이트의 제목 입니다")
        String name;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateHighlightResponseDTO {

        @Schema(description = "하이라이트 식별 ID 입니다")
        Long highlightId;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetHighlightResponseDTO {

        @Schema(description = "스토리 이미지 입니다 <br>," +
                "추후 구현 방식에 따라 응답이 달라질 수 있습니다")
        List<String> imagePaths;

    }
}
