package com.example.momogum.service.appointment;


import com.example.momogum.converter.appointment.AppointmentNameConverter;
import com.example.momogum.domain.appointment.AppointmentName;
import com.example.momogum.repository.appoinmentRepo.AppointmentNameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.momogum.web.dto.appointment.AppointMentDTO.*;

@Service
@RequiredArgsConstructor
public class AppointmentNameService {

    private final AppointmentNameRepository repository;
    private final AppointmentNameConverter converter;

    /**
     * [약속 식사 모임 이름 정하기]
     * @param request CreateMealPlanNameDTO
     * @return appointmentId
     */
    public Long creatAppointmentName(AppointmentNameDTO request) {

        AppointmentName appointmentName = converter.convert(request);

        return repository.save(appointmentName).getId();
    }


}
