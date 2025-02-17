package com.example.momogum.web.controller.appointment;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.service.appointmentService.AppointmentService;
import com.example.momogum.web.dto.appointment.AppointmentDTO.AppointmentDetailsDTO;
import com.example.momogum.web.dto.appointment.AppointmentDTO.PendingInvitationDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.momogum.web.dto.appointment.AppointmentDTO.*;

@RestController
@RequestMapping("/appointment")
@Tag(name = "약속잡기 조회 API")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;


    @Operation(summary = "초대장 확인 API",
            description = "약속 정보, 초대된 친구, 선택된 카드 정보를 반환합니다.")
    @PostMapping("/{appointmentId}/details")
    public ApiResponse<AppointmentDetailsDTO> createWholeAppointment(@PathVariable Long appointmentId) {
        AppointmentDetailsDTO response = appointmentService.getAppointmentDetails(appointmentId);
        return ApiResponse.onSuccess(response);
    }

    public ApiResponse<List<PendingInvitationDTO>> getPendingInvitations(@RequestParam Long userId) {
        return ApiResponse.onSuccess(appointmentService.getPendingInvitations(userId));
    }

    public ApiResponse<List<AcceptedInvitationDTO>> getAcceptedInvitations(@RequestParam Long userId) {
        return ApiResponse.onSuccess(appointmentService.getAcceptedAppointments(userId));
    }


}
