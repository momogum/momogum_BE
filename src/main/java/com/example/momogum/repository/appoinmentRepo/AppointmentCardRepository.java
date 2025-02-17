package com.example.momogum.repository.appoinmentRepo;

import com.example.momogum.domain.appointment.Appointment;
import com.example.momogum.domain.appointment.AppointmentCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppointmentCardRepository extends JpaRepository<AppointmentCard,Long> {
    Optional<AppointmentCard> findByAppointment(Appointment appointment);
}
