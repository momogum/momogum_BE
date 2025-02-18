package com.example.momogum.web.controller.appointment;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.GeneralException;
import com.example.momogum.domain.utils.JwtUtil;
import com.example.momogum.service.UserService;
import com.example.momogum.service.appointmentService.AppointmentInviteService;
import com.example.momogum.web.dto.appointment.AppointmentInviteDTO.AppointmentInviteRequestDTO;
import com.example.momogum.web.dto.appointment.AppointmentInviteDTO.AppointmentInviteResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointment")
@RequiredArgsConstructor
@Tag(name = "약속잡기 친구 초대 API")
public class AppointmentInviteController {

    private final AppointmentInviteService appointmentInviteService;
    private final UserService userService;

    /**
     * GET /Appointment/{appointmentId}/invites
     * 약속에 초대 가능한 친구 목록 반환
     */
    @Operation(
            summary = "초대 가능한 친구 목록 반환",
            description = """
                        지정된 약속 ID에 대해 초대 가능한 친구 목록을 반환합니다.
                        이미 초대된 친구는 목록에 포함되지 않습니다.
                    """
    )
    @GetMapping("/{appointmentId}/invites")
    public ResponseEntity<List<AppointmentInviteResponseDTO>> getFriendsForInvitations(
            @PathVariable Long appointmentId,
            @RequestParam Long userId
    ) {
        Long validUserId = userService.validateUserId(userId);
        List<AppointmentInviteResponseDTO> invitations = appointmentInviteService.getFriendsForInvitation(appointmentId, validUserId);
        return ResponseEntity.ok(invitations);
    }

    /**
     * POST /Appointment/invites
     * 약속에 친구 초대
     */
    @Operation(
            summary = "약속에 친구 초대",
            description = """
                        요청 본문에는 AppointmentInviteRequestDTO 형태의 데이터를 포함합니다.
                        데이터는 약속 ID와 초대할 친구들의 username 리스트로 구성됩니다.
                    """
    )
    @PostMapping("/invites")
    public ResponseEntity<List<AppointmentInviteResponseDTO>> inviteFriends(
            @RequestBody AppointmentInviteRequestDTO request
    ) {
        List<AppointmentInviteResponseDTO> invitedFriends = appointmentInviteService.inviteFriends(request);
        return ResponseEntity.ok(invitedFriends);
    }

}
