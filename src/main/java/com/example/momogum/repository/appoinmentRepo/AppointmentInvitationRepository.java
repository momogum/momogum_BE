package com.example.momogum.repository.appoinmentRepo;

import com.example.momogum.domain.appointment.AppointmentInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentInvitationRepository extends JpaRepository<AppointmentInvitation,Long> {
}
