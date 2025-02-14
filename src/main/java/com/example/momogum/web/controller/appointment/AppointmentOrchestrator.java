package com.example.momogum.web.controller.appointment;

import com.example.momogum.domain.common.enums.CardCategory;
import com.example.momogum.service.appointmentService.AppointmentCardService;
import com.example.momogum.service.appointmentService.AppointmentInviteService;
import com.example.momogum.service.appointmentService.AppointmentNameService;
import com.example.momogum.web.dto.appointment.AppointmentCardDTO;
import com.example.momogum.web.dto.appointment.AppointmentCardDTO.AppointmentCardResponseDTO;
import com.example.momogum.web.dto.appointment.AppointmentInviteDTO;
import com.example.momogum.web.dto.appointment.AppointmentInviteDTO.AppointmentInviteRequestDTO;
import com.example.momogum.web.dto.appointment.AppointmentInviteDTO.AppointmentInviteResponseDTO;
import com.example.momogum.web.dto.appointment.AppointmentOrchestratorDTO;
import com.example.momogum.web.dto.appointment.AppointmentOrchestratorDTO.AppointmentOrchestratorRequestDTO;
import com.example.momogum.web.dto.appointment.AppointmentOrchestratorDTO.AppointmentOrchestratorResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentOrchestrator {

    private final AppointmentInviteService inviteService;
    private final AppointmentCardService cardService;
    private final AppointmentNameService nameService;

    @Transactional
    public AppointmentOrchestratorResponseDTO createWholeAppointment(AppointmentOrchestratorRequestDTO request) {

        // 1) 초대 로직 수행
        AppointmentInviteRequestDTO inviteRequest = AppointmentInviteRequestDTO.builder()
                .appointmentId(request.getAppointmentId())
                .nicknames(request.getNicknames())
                .build();

        // 친구 초대
        List<AppointmentInviteResponseDTO> invitedFriends = inviteService.inviteFriends(inviteRequest);

        // 2) 카드 로직 수행 (cardCategory 이용한 특정 카드 목록 조회)
        CardCategory category = CardCategory.valueOf(request.getCardCategory().getCategory());
        List<AppointmentCardResponseDTO> selectedCards = cardService.getCards(category);

        // 3) 약속 식사(이름) 수정
        Long appointmentId = nameService.creatAppointmentName(request.getAppointmentName());

        // 4) 전체 결과를 AppointmentOrchestratorResponseDTO 형식으로 반환
        return AppointmentOrchestratorResponseDTO.builder()
                .invitedFriends(invitedFriends)
                .selectedCards(selectedCards)
                .appointmentNameId(appointmentId)
                .build();

    }
}
