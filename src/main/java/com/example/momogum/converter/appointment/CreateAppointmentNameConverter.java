package com.example.momogum.converter.appointment;

import com.example.momogum.converter.Converter;
import com.example.momogum.domain.appointment.CreateAppointmentName;
import com.example.momogum.web.dto.appointment.AppointMentDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.example.momogum.web.dto.appointment.AppointMentDTO.*;

/**
 * 약속 이름 정하기 부분 converter
 */
@Component
public class CreateAppointmentNameConverter implements Converter<CreateAppointmentNameDTO, CreateAppointmentName> {

    @Override
    public CreateAppointmentName convert(CreateAppointmentNameDTO request) {

        return CreateAppointmentName.builder()
                .name(request.getName())
                .menu(request.getMenu())
                .date(LocalDateTime.from(request.getDate()))
                .location(request.getLocation())
                .notes(request.getNotes())
                .build();
    }

}
