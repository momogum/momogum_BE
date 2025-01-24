package com.example.momogum.service.appointment;


import com.example.momogum.converter.appointment.CreateAppointmentNameConverter;
import com.example.momogum.domain.appointment.CreateAppointmentName;
import com.example.momogum.repository.appoinmentRepo.CreateAppointmentNameRepository;
import com.example.momogum.web.dto.appointment.AppointMentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.example.momogum.web.dto.appointment.AppointMentDTO.*;

@Service
@RequiredArgsConstructor
public class CreateAppointmentNameService {

    private final CreateAppointmentNameRepository repository;
    private final CreateAppointmentNameConverter converter;

    /**
     * [약속 식사 모임 이름 정하기]
     * @param request CreateMealPlanNameDTO
     * @return appointmentId
     */
    public Long creatAppointmentName(CreateAppointmentNameDTO request) {

        CreateAppointmentName appointmentName = converter.convert(request);

        return repository.save(appointmentName).getId();
    }


}
