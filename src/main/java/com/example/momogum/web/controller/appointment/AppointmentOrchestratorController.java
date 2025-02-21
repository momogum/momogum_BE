package com.example.momogum.web.controller.appointment;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.service.appointmentService.orchestrator.AppointmentOrchestrator;
import com.example.momogum.web.dto.appointment.AppointmentOrchestratorDTO.AppointmentOrchestratorRequestDTO;
import com.example.momogum.web.dto.appointment.AppointmentOrchestratorDTO.AppointmentOrchestratorResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appointment")
@Tag(name = "전체 약속잡기 API")
@RequiredArgsConstructor
public class AppointmentOrchestratorController {

    private final AppointmentOrchestrator orchestrator;

    @Operation(summary = "전체 약속잡기 실행 API",
            description = "친구 초대, 카드 선택, 약속 식사 모임 이름 저장 등 전체 약속잡기 프로세스를 실행하고, 그 결과를 반환합니다.")
    @PostMapping(value = "/whole", produces = "application/json; charset=UTF-8")
    public ApiResponse<AppointmentOrchestratorResponseDTO> createWholeAppointment(
            @RequestBody AppointmentOrchestratorRequestDTO request) {
        AppointmentOrchestratorResponseDTO WholeAppointment = orchestrator.createWholeAppointment(request);
        return ApiResponse.onSuccess(WholeAppointment);
    }

}