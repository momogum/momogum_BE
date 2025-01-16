package com.example.momogum.repository.appointmentManagerRepo;

import com.example.momogum.domain.AppointmentManager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentManagerRepository extends JpaRepository<AppointmentManager,Long> {
}
