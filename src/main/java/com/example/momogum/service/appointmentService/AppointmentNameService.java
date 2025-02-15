package com.example.momogum.service.appointmentService;


import com.example.momogum.converter.appointmentConverter.AppointmentNameConverter;
import com.example.momogum.domain.appointment.AppointmentName;
import com.example.momogum.repository.appoinmentRepo.AppointmentNameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.momogum.web.dto.appointment.AppointmentNameDTO.*;

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
    public Long creatAppointmentName(AppointmentNameRequestDTO request) {

        AppointmentName appointmentName = converter.convert(request);

        return repository.save(appointmentName).getId();
    }


}
