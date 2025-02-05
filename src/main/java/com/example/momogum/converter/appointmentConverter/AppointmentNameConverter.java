package com.example.momogum.converter.appointmentConverter;

import com.example.momogum.converter.Converter;
import com.example.momogum.domain.appointment.AppointmentName;
import com.example.momogum.web.dto.appointment.AppointmentNameDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.example.momogum.web.dto.appointment.AppointMentDTO.*;
import static com.example.momogum.web.dto.appointment.AppointmentNameDTO.*;

/**
 * 약속 이름 정하기 부분 converter
 */
@Component
public class AppointmentNameConverter implements Converter<AppointmentNameResponseDTO, AppointmentName> {

    @Override
    public AppointmentName convert(AppointmentNameResponseDTO request) {

        return AppointmentName.builder()
                .name(request.getName())
                .menu(request.getMenu())
                .date(LocalDateTime.from(request.getDate()))
                .location(request.getLocation())
                .notes(request.getNotes())
                .build();
    }

}
