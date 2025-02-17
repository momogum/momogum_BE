package com.example.momogum.web.dto.appointment;

import com.example.momogum.domain.common.enums.CardCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AppointmentCardDTO {

    /**
     * 카드 1장 선택 Request DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AppointmentCardRequestDTO {
        private String imageUrl;
        private CardCategory category;
    }

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
