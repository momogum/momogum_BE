package com.example.momogum.service.appointmentService;

import com.example.momogum.domain.appointment.Appointment;
import com.example.momogum.repository.appoinmentRepo.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }
}
