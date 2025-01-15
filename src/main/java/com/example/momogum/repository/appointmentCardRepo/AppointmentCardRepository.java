package com.example.momogum.repository.appointmentCardRepo;

import com.example.momogum.domain.AppointmentCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentCardRepository extends JpaRepository<AppointmentCard,Long> {
}
