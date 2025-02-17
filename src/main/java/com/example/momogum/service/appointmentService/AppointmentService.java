package com.example.momogum.service.appointmentService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.GeneralException;
import com.example.momogum.converter.appointmentConverter.AppointmentConverter;
import com.example.momogum.domain.appointment.Appointment;
import com.example.momogum.repository.appoinmentRepo.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentConverter appointmentConverter;

    /**
     * 빈 Appointment 객체를 생성하고, ID를 반환
     */
    @Transactional
    public Appointment createEmptyAppointment() {
        // 1️⃣ 빈 Appointment 객체 생성 (아직 데이터 없음)
        Appointment appointment = appointmentConverter.toEmptyEntity();

        // 2️⃣ DB에 저장 (ID를 생성하기 위해)
        appointment = appointmentRepository.save(appointment);

        return appointment;
    }

    /**
     * ID로 Appointment 조회
     */
    @Transactional(readOnly = true)
    public Appointment findById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.APPOINTMENT_NOT_EXIST));
    }

    /**
     * 업데이트된 Appointment 저장
     */
    @Transactional
    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }
}
