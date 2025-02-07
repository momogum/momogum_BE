package com.example.momogum.web.controller.appointment;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.service.appointmentService.AppointmentNameService;
import com.example.momogum.web.dto.appointment.AppointMentDTO.CreateAppointmentResponseDTO;
import com.example.momogum.web.dto.appointment.AppointmentNameDTO.AppointmentNameResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appointment")
@Tag(name = "약속잡기 모임 이름 정하기 API")
@RequiredArgsConstructor
public class AppointmentNameController {

    private final AppointmentNameService appointmentNameService;


    @Operation(summary = "모임 이름 정하기 API",
            description = "CreateAppointmentNameDTO을 통해 값을 한 번에 입력받습니다.")
    @PostMapping("/name")
    public ApiResponse<CreateAppointmentResponseDTO> createAppointmentName(@RequestBody @Valid AppointmentNameResponseDTO request) {
        Long appointmentNameId = appointmentNameService.creatAppointmentName(request);
        // 성공 응답 반환
        return ApiResponse.onSuccess(
                CreateAppointmentResponseDTO.builder()
                        .appointmentId(appointmentNameId)
                        .build()
        );
    }
}
