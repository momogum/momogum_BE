package com.example.momogum.web.dto.appointment;

import com.example.momogum.domain.common.enums.InvitationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class AppointmentInviteDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AppointmentInviteRequestDTO {

        @Schema(description = "약속 ID")
        private Long appointmentId;

        @Schema(description = "초대할 친구들의 사용자 이름 리스트")
        private List<String> nicknames;

    }


    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AppointmentInviteResponseDTO {

        @Schema(description = "사용자 닉네임")
        private String nickname;

        @Schema(description = "사용자 이름")
        private String name;

        @Schema(description = "프로필 이미지")
        private String profileImage;

        @Schema(description = "초대 여부")
        private InvitationStatus status;

    }
}
