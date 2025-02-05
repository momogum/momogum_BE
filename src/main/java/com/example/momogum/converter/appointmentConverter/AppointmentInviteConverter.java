package com.example.momogum.converter.appointmentConverter;

import com.example.momogum.domain.UserEntity;
import com.example.momogum.web.dto.appointment.AppointmentInviteDTO.AppointmentInviteResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class AppointmentInviteConverter {

    public AppointmentInviteResponseDTO toResponseDTO(UserEntity user,  boolean isInvited) {
        return AppointmentInviteResponseDTO.builder()
                .username(user.getNickname())
                .name(user.getName())
                .profileImage(user.getProfileImage().getImageLink())
                .isInvited(isInvited)
                .build();
    }
}
