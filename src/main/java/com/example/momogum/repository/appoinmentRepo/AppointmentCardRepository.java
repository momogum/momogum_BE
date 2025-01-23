package com.example.momogum.repository.appoinmentRepo;

import com.example.momogum.domain.common.appointment.AppointmentCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentCardRepository extends JpaRepository<AppointmentCard,Long> {
}
