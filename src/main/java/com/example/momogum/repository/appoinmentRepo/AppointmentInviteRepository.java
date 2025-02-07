package com.example.momogum.repository.appoinmentRepo;

import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.appointment.AppointmentInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentInviteRepository extends JpaRepository<AppointmentInvitation, Long> {

    boolean existsByAppointmentIdAndUserEntity(Long appointmentId, UserEntity user);

    List<AppointmentInvitation> findByAppointmentId(Long appointmentId);

}
