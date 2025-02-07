package com.example.momogum.web.dto.appointment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class AppointmentNameDTO {

    /**
     *  약속 식사 모임 이름 정하기 DTO
     *  엔티티 : CreateAppointmentName
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AppointmentNameResponseDTO {

        @Schema(description = "식사 모임 이름")
        @NotBlank(message = "필수 작성 항목입니다.")
        String name;

        @Schema(description = "식사 메뉴")
        @NotBlank(message = "필수 작성 항목입니다.")
        String menu;

        @Schema(description = "식사 일정")
        @NotNull(message = "필수 작성 항목입니다.")
        LocalDateTime date;

        @Schema(description = "식사 모임 위치")
        @NotBlank(message = "필수 작성 항목입니다.")
        String location;

        @Schema(description = "특별한 소식")
        String notes;

    }
}
