package com.example.momogum.converter.appointmentConverter;

import com.example.momogum.converter.Converter;
import com.example.momogum.domain.appointment.AppointmentName;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.example.momogum.web.dto.appointment.AppointmentNameDTO.*;

/**
 * 약속 이름 정하기 부분 converter
 */
@Component
public class AppointmentNameConverter implements Converter<AppointmentNameRequestDTO, AppointmentName> {

    @Override
    public AppointmentName convert(AppointmentNameRequestDTO request) {

        return AppointmentName.builder()
                .name(request.getName())
                .menu(request.getMenu())
                .date(LocalDateTime.from(request.getDate()))
                .location(request.getLocation())
                .notes(request.getNotes())
                .build();
    }

}
