package com.example.momogum.web.dto.appointment;

import com.example.momogum.domain.common.enums.CardCategory;
import com.example.momogum.web.dto.appointment.AppointmentCardDTO.AppointmentCardResponseDTO;
import com.example.momogum.web.dto.appointment.AppointmentNameDTO.AppointmentNameRequestDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.example.momogum.web.dto.appointment.AppointmentInviteDTO.*;

public class AppointmentOrchestratorDTO {

    /**
     * 약속 생성 후 ID 반환 하는 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AppointmentOrchestratorRequestDTO {

        @Schema(description = "현재 사용자 ID 입니다.")
        private Long userId;

        @Schema(description = "현재 약속 ID 입니다.")
        private Long appointmentId;

        @Schema(description = "현재 사용자 nicknames 입니다.")
        private List<String> nicknames;

        @Schema(description = "약속 카드 카테고리 입니다.")
        private CardCategory cardCategory;

        @Schema(description = "선택한 카드 이미지 URL 입니다.")
        private String selectedCardUrl;

        @Schema(description = "약속 식사 모임 이름 정하기 DTO 입니다.")
        private AppointmentNameRequestDTO appointmentName;
    }

    /**
     * 약속 생성 후 ID 반환 하는 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AppointmentOrchestratorResponseDTO {

        @Schema(description = "초대된 친구 리스트입니다.")
        private List<AppointmentInviteResponseDTO> invitedFriends;

        @Schema(description = "선택된 카드 목록입니다.")
        private List<AppointmentCardResponseDTO> selectedCards;

        @Schema(description = "저장된 약속 ID 입니다.")
        private Long appointmentId;

    }
}
