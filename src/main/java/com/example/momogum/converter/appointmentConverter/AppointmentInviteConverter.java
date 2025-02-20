package com.example.momogum.converter.appointmentConverter;

import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.appointment.Appointment;
import com.example.momogum.domain.appointment.AppointmentInvitation;
import com.example.momogum.domain.common.enums.InvitationStatus;
import com.example.momogum.web.dto.appointment.AppointmentInviteDTO.AppointmentInviteResponseDTO;
import org.springframework.stereotype.Component;


@Component
public class AppointmentInviteConverter {

    public AppointmentInviteResponseDTO toResponseDTO(UserEntity user, InvitationStatus status) {
        return AppointmentInviteResponseDTO.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .name(user.getName())
                .profileImage(user.getProfileImage().getImageLink())
                .status(status)
                .build();
    }
}
