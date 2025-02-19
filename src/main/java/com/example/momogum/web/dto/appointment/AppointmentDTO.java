package com.example.momogum.web.dto.appointment;


import com.example.momogum.domain.appointment.AppointmentCard;
import com.example.momogum.web.dto.appointment.AppointmentCardDTO.AppointmentCardResponseDTO;
import com.example.momogum.web.dto.appointment.AppointmentInviteDTO.AppointmentInviteResponseDTO;
import com.example.momogum.web.dto.user.UserDTO.UserResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AppointmentDTO {


    /**
     * 약속 생성 후 ID 반환 하는 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateAppointmentResponseDTO {

        @Schema(description = "약속잡기 식별 ID 입니다.")
        Long appointmentId;
    }


    /**
     * 약속 잡기 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AppointmentResponseDTO {

        @Schema(description = "약속잡기 식별 ID 입니다.")
        Long id;

        @Schema(description = "약속 제안입니다. ex) 커피 한 잔 할까요?")
        AppointmentCard cards;

        @Schema(description = "약속 제목입니다.")
        String title;

        @Schema(description = "식사 메뉴입니다.")
        String menu;

        @Schema(description = "약속 날짜입니다.")
        LocalDate date;

        @Schema(description = "약속 위치입니다.")
        String location;

        @Schema(description = "추가 메모입니다.")
        String notes;

        @Schema(description = "약속 생성 날짜입니다.")
        String createdAt;

        @Schema(description = "참여자 목록입니다.")
        List<UserResponseDTO> users;

    }

    /**
     * 초대장 확인 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AppointmentDetailsDTO {

        @Schema(description = "저장된 약속 ID 입니다.")
        private Long appointmentId;

        @Schema(description = "약속 상세 정보입니다.")
        private AppointmentInfoDTO appointmentInfo;

        @Schema(description = "선택된 카드 정보입니다.")
        private AppointmentCardResponseDTO selectedCard;

        @Schema(description = "초대된 친구 리스트입니다.")
        private List<AppointmentInviteResponseDTO> invitedFriends;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AppointmentInfoDTO {

        @Schema(description = "약속 이름")
        private String name;

        @Schema(description = "식사 메뉴")
        private String menu;

        private LocalDateTime time;

        @Schema(description = "약속 날짜")
        private LocalDateTime date;

        @Schema(description = "약속 장소")
        private String location;

        @Schema(description = "추가 메모")
        private String notes;
    }

}
