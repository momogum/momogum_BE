package com.example.momogum.service.appointmentService.orchestrator;

import com.example.momogum.converter.appointmentConverter.AppointmentConverter;
import com.example.momogum.converter.appointmentConverter.AppointmentInviteConverter;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.appointment.Appointment;
import com.example.momogum.domain.appointment.AppointmentInvitation;
import com.example.momogum.domain.common.enums.CardCategory;
import com.example.momogum.domain.common.enums.InvitationStatus;
import com.example.momogum.domain.utils.S3UrlProvider;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.service.appointmentService.AppointmentCardService;
import com.example.momogum.service.appointmentService.AppointmentInviteService;
import com.example.momogum.service.appointmentService.AppointmentNameService;
import com.example.momogum.service.appointmentService.AppointmentService;
import com.example.momogum.web.dto.appointment.AppointmentCardDTO.AppointmentCardResponseDTO;
import com.example.momogum.web.dto.appointment.AppointmentInviteDTO;
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
    private final AppointmentService appointmentService;
    private final AppointmentConverter appointmentConverter;

    @Transactional
    public AppointmentOrchestratorResponseDTO createWholeAppointment(AppointmentOrchestratorRequestDTO request) {

        // 1. Appointment 객체 생성 (연관 데이터 없이 먼저 생성)
        Appointment appointment = appointmentConverter.toEntity(request);

        // 2. 초대된 친구 추가
        inviteService.inviteFriends(AppointmentInviteDTO.AppointmentInviteRequestDTO.builder()
                .appointmentId(request.getAppointmentId())
                .nicknames(request.getNicknames())
                .build());

        // 3.  카드 정보 조회 (S3 기반)
        List<AppointmentCardResponseDTO> selectedCards = cardService.getCards(request.getCardCategory());

        // 4. 약속 이름 저장
        nameService.saveAppointmentName(request.getAppointmentName());

        // 5. DB에 저장
        appointment = appointmentService.save(appointment);

        return appointmentConverter.toResponseDTO(appointment, selectedCards);


    }
}
