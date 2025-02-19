package com.example.momogum.service.appointmentService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.GeneralException;
import com.example.momogum.converter.appointmentConverter.AppointmentConverter;
import com.example.momogum.domain.appointment.Appointment;
import com.example.momogum.domain.appointment.AppointmentCard;
import com.example.momogum.domain.appointment.AppointmentInvitation;
import com.example.momogum.domain.common.enums.InvitationStatus;
import com.example.momogum.repository.appoinmentRepo.AppointmentCardRepository;
import com.example.momogum.repository.appoinmentRepo.AppointmentInviteRepository;
import com.example.momogum.repository.appoinmentRepo.AppointmentRepository;
import com.example.momogum.web.dto.appointment.AppointmentCardDTO.AppointmentCardResponseDTO;
import com.example.momogum.web.dto.appointment.AppointmentDTO.AppointmentDetailsDTO;
import com.example.momogum.web.dto.appointment.AppointmentOrchestratorDTO.AppointmentOrchestratorResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentConverter appointmentConverter;
    private final AppointmentInviteRepository appointmentInviteRepository;
    private final AppointmentCardRepository appointmentCardRepository;

    /**
     * 빈 Appointment 객체를 생성하고, ID를 반환
     */
    @Transactional
    public Appointment createEmptyAppointment() {
        // 1️⃣ 빈 Appointment 객체 생성 (아직 데이터 없음)
        Appointment appointment = appointmentConverter.toEmptyEntity();

        // 2️⃣ DB에 저장 (ID를 생성하기 위해)
        appointment = appointmentRepository.save(appointment);

        return appointment;
    }

    /**
     * ID로 Appointment 조회
     */
    public Appointment findById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.APPOINTMENT_NOT_EXIST));
    }

    /**
     * 업데이트된 Appointment 저장
     */
    @Transactional
    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    /**
     * 초대장 조회
     * @param appointmentId
     * @return AppointmentDetailsDTO 객체
     */
    public AppointmentDetailsDTO getAppointmentDetails(Long appointmentId) {

        //약속 조회
        Appointment appointment = findById(appointmentId);

        //초대된 친구 목록 조회
        List<AppointmentInvitation> invitations = appointmentInviteRepository.findByAppointmentId(appointmentId);

        //카드 조회
        AppointmentCard selectedCard = appointmentCardRepository.findByAppointment(appointment)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CARD_NOT_EXIST));

        return appointmentConverter.toDetailsDTO(appointment, invitations, selectedCard);
    }

    /**
     * 다가오는 확정된 약속 조회 (ACCEPTED)
     */
    public List<AppointmentOrchestratorResponseDTO> getAcceptedAppointments(Long userId) {
        return appointmentInviteRepository.findAppointmentsByStatus(userId, InvitationStatus.ACCEPTED)
                .stream()
                .map(appointment -> appointmentConverter.toResponseDTO(appointment, appointment.getSelectedCards()
                        .stream()
                        .map(card -> AppointmentCardResponseDTO.builder()
                                .category(card.getCategory().getCategory())
                                .imageUrl(card.getImageUrl())
                                .build())
                        .toList()))
                .toList();
    }


    /**
     * 확정된 약속 조회 (Confirmed)
     */
    public List<AppointmentOrchestratorResponseDTO> getConfirmedAppointments(Long userId) {
        return appointmentInviteRepository.findAppointmentsByStatus(userId, InvitationStatus.CONFIRMED)
                .stream()
                .map(appointment -> appointmentConverter.toResponseDTO(appointment, appointment.getSelectedCards()
                        .stream()
                        .map(card -> AppointmentCardResponseDTO.builder()
                                .category(card.getCategory().getCategory())
                                .imageUrl(card.getImageUrl())
                                .build())
                        .toList()))
                .toList();
    }
}
