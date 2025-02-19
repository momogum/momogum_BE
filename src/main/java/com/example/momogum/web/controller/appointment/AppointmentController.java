package com.example.momogum.web.controller.appointment;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.security.CustomUserDetails;
import com.example.momogum.service.appointmentService.AppointmentService;
import com.example.momogum.web.dto.appointment.AppointmentDTO.AppointmentDetailsDTO;
import com.example.momogum.web.dto.appointment.AppointmentDTO.PendingInvitationDTO;
import com.example.momogum.web.dto.appointment.AppointmentOrchestratorDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.momogum.web.dto.appointment.AppointmentDTO.*;
import static com.example.momogum.web.dto.appointment.AppointmentOrchestratorDTO.*;

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

    @Operation(summary = "약속 확정 API",
            description = "현재 보낸 초대장을 확인하고 약속 확정을 합니다.")
    @PostMapping("/{appointmentId}/confirmed")
    public ApiResponse<Long> confirmedAppointment(@PathVariable Long appointmentId) {
        Long resultAppointmentId = appointmentService.confirmedAppointment(appointmentId);
        return ApiResponse.onSuccess(resultAppointmentId);
    }

    @Operation(summary = "약속 삭제 API",
            description = "현재 선택된 약속을 삭제합니다.")
    @PostMapping("/{appointmentId}/delete")
    public ApiResponse<String> deleteAppointment(@PathVariable Long appointmentId) {
        appointmentService.deleteAppointment(appointmentId);
        return ApiResponse.onSuccess("약속이 삭제되었습니다.");
    }


    // UserID를 @PathVariable 방식에서 @AuthenticationPrincipal로 대체
    // 엔드포인트에서 userId를 직접 받는 방식에서 JWT에서 자동으로 가져오도록 수정
    @Operation(summary = "수락 대기중인 식사 약속 조회 API",
            description = "현재 사용자가 초대받았으나 아직 확정되지 않은 약속 목록을 조회합니다.")
    @GetMapping("/accept")
    public ApiResponse<List<AppointmentOrchestratorResponseDTO>> getAcceptedInvitations(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        return ApiResponse.onSuccess(appointmentService.getAllAcceptedAppointments(userId));
    }

    @Operation(summary = "확정된 식사 약속 조회 API",
            description = "현재 사용자가 확정한 승인된 전체 식사 약속 목록을 조회합니다.")
    @GetMapping("/confirmed")
    public ApiResponse<List<AppointmentOrchestratorResponseDTO>> getConfirmedInvitations(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        return ApiResponse.onSuccess(appointmentService.getConfirmedAppointments(userId));
    }
}


