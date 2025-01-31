package com.example.momogum.repository.appoinmentRepo;

import com.example.momogum.domain.appointment.AppointmentName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentNameRepository extends JpaRepository<AppointmentName,Long> {
}
