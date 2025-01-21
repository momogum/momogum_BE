package com.example.momogum.repository.appoinmentRepo;

import com.example.momogum.domain.common.appointment.AppointmentManager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentManagerRepository extends JpaRepository<AppointmentManager,Long> {
}
