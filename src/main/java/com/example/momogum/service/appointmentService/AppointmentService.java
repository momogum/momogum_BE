package com.example.momogum.service.appointmentService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.GeneralException;
import com.example.momogum.converter.appointmentConverter.AppointmentConverter;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.appointment.Appointment;
import com.example.momogum.domain.appointment.AppointmentCard;
import com.example.momogum.domain.appointment.AppointmentInvitation;
import com.example.momogum.domain.common.enums.InvitationStatus;
import com.example.momogum.repository.appoinmentRepo.AppointmentCardRepository;
import com.example.momogum.repository.appoinmentRepo.AppointmentInviteRepository;
import com.example.momogum.repository.appoinmentRepo.AppointmentRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.appointment.AppointmentCardDTO.AppointmentCardResponseDTO;
import com.example.momogum.web.dto.appointment.AppointmentDTO.AppointmentDetailsDTO;
import com.example.momogum.web.dto.appointment.AppointmentDTO.AppointmentMainPageResponseDTO;
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
    private final UserEntityRepository userEntityRepository;

    /**
     * 빈 Appointment 객체를 생성하고, ID를 반환
     */
    @Transactional
    public Appointment createTemporaryAppointment() {

        // 더미 데이터 이용
        UserEntity defaultUser = userEntityRepository.findById(9L)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NO_RESULT_FOUND));

        Appointment appointment = appointmentConverter.toTemporaryEntity(defaultUser);

        return appointmentRepository.save(appointment);
    }

    /**
     * 업데이트된 Appointment 저장
     */
    @Transactional
    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    /**
     * ID로 Appointment 조회
     */
    public Appointment findById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.APPOINTMENT_NOT_EXIST));
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
     * 다가오는 확정된 전체 약속 조회 (ACCEPTED)
     */
    public List<AppointmentMainPageResponseDTO> getAllAcceptedAppointments(Long userId) {
        return appointmentInviteRepository.findAppointmentsByStatus(userId, InvitationStatus.ACCEPTED)
                .stream()
                .map(appointment -> appointmentConverter.toMainPageDTO(appointment, appointment.getSelectedCards()
                        .stream()
                        .map(card -> AppointmentCardResponseDTO.builder()
                                .category(card.getCategory().getCategory())
                                .imageUrl(card.getImageUrl())
                                .build())
                        .toList()))
                .toList();
    }


    /**
     * 확정된 약속 전체 조회 (Confirmed)
     */
    public List<AppointmentMainPageResponseDTO> getConfirmedAppointments(Long userId) {
        return appointmentInviteRepository.findAppointmentsByStatus(userId, InvitationStatus.CONFIRMED)
                .stream()
                .map(appointment -> appointmentConverter.toMainPageDTO(appointment, appointment.getSelectedCards()
                        .stream()
                        .map(card -> AppointmentCardResponseDTO.builder()
                                .category(card.getCategory().getCategory())
                                .imageUrl(card.getImageUrl())
                                .build())
                        .toList()))
                .toList();
    }


    /**
     * 약속 확정 후 상태 변경 ACCEPTED -> CONFIRMED
     * @param appointmentId
     */
    @Transactional
    public Long confirmedAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.APPOINTMENT_NOT_EXIST));

        appointment.getInvitations().forEach(invite -> invite.setStatus(InvitationStatus.CONFIRMED));

        appointmentRepository.save(appointment);

        return appointment.getId();
    }

    /**
     * 약속 삭제
     * @param appointmentId
     */
    @Transactional
    public void deleteAppointment(Long appointmentId) {
        Appointment appointment = findById(appointmentId);
        appointmentRepository.delete(appointment);
    }

}
