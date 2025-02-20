package com.example.momogum.service.appointmentService.orchestrator;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.GeneralException;
import com.example.momogum.converter.appointmentConverter.AppointmentConverter;
import com.example.momogum.domain.appointment.Appointment;
import com.example.momogum.domain.common.enums.InvitationStatus;
import com.example.momogum.repository.appoinmentRepo.AppointmentRepository;
import com.example.momogum.service.appointmentService.AppointmentCardService;
import com.example.momogum.service.appointmentService.AppointmentInviteService;
import com.example.momogum.service.appointmentService.AppointmentNameService;
import com.example.momogum.service.appointmentService.AppointmentService;
import com.example.momogum.web.dto.appointment.AppointmentCardDTO;
import com.example.momogum.web.dto.appointment.AppointmentInviteDTO.AppointmentInviteRequestDTO;
import com.example.momogum.web.dto.appointment.AppointmentOrchestratorDTO.AppointmentOrchestratorRequestDTO;
import com.example.momogum.web.dto.appointment.AppointmentOrchestratorDTO.AppointmentOrchestratorResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AppointmentOrchestrator {

    private final AppointmentInviteService inviteService;
    private final AppointmentCardService cardService;
    private final AppointmentService appointmentService;
    private final AppointmentConverter appointmentConverter;

    @Transactional
    public AppointmentOrchestratorResponseDTO createWholeAppointment(AppointmentOrchestratorRequestDTO request) {

        // 1. Appointment 객체 생성 (연관 데이터 없이 먼저 생성)
        Appointment appointment = appointmentService.createTemporaryAppointment();

        // 2. 초대된 친구 추가
        inviteService.inviteFriends(AppointmentInviteRequestDTO.builder()
                .appointmentId(appointment.getId())
                .userIds(request.getUserIds())
                .build());

        // 3.  카드 정보 조회 (S3 기반)
        AppointmentCardDTO.AppointmentCardResponseDTO selectCard = cardService.selectCard(
                appointment.getId(), new AppointmentCardDTO.AppointmentCardRequestDTO(request.getSelectedCardUrl(), request.getCardCategory()));

        // 4. 약속 이름 저장
        appointmentService.updateAppointment(appointment.getId(), request);


        // 6. 초대 상황 변경
        inviteService.updateInvitationStatus(appointment.getId(), InvitationStatus.ACCEPTED);

        return appointmentConverter.toResponseDTO(appointment, selectCard);


    }
}

