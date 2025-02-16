package com.example.momogum.converter.appointmentConverter;

import com.example.momogum.domain.appointment.Appointment;
import com.example.momogum.web.dto.appointment.AppointmentCardDTO.AppointmentCardResponseDTO;
import com.example.momogum.web.dto.appointment.AppointmentInviteDTO.AppointmentInviteResponseDTO;
import com.example.momogum.web.dto.appointment.AppointmentOrchestratorDTO.AppointmentOrchestratorRequestDTO;
import com.example.momogum.web.dto.appointment.AppointmentOrchestratorDTO.AppointmentOrchestratorResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AppointmentConverter {

    public Appointment toEntity(AppointmentOrchestratorRequestDTO request) {
        return   Appointment.builder()
                .name(request.getAppointmentName().getName())
                .menu(request.getAppointmentName().getMenu())
                .date(request.getAppointmentName().getDate())
                .location(request.getAppointmentName().getLocation())
                .notes(request.getAppointmentName().getNotes())
                .build();
    }

    public AppointmentOrchestratorResponseDTO toResponseDTO(
            Appointment appointment, List<AppointmentCardResponseDTO> selectedCards) {
        return AppointmentOrchestratorResponseDTO.builder()
                .appointmentId(appointment.getId())
                .invitedFriends(appointment.getInvitations().stream()
                        .map(invite -> AppointmentInviteResponseDTO.builder()
                                .nickname(invite.getUserEntity().getNickname())
                                .name(invite.getUserEntity().getName())
                                .profileImage(invite.getUserEntity().getProfileImage() != null
                                        ? invite.getUserEntity().getProfileImage().getImageLink()
                                        : null)
                                .status(invite.getStatus())
                                .build())
                        .toList())
                .selectedCards(selectedCards)
                .build();
    }
}