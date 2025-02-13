package com.example.momogum.web.dto.appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AppointmentCardDTO {

    /**
     * S3 -> 카드 조회 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AppointmentCardResponseDTO {
        private String category;       // 카드 카테고리 (예: 기본, 재미)
        private String imageUrl;   // S3 이미지 URL
    }
}
