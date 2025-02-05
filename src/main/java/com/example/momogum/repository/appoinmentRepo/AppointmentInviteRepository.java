package com.example.momogum.repository.appoinmentRepo;

import com.example.momogum.domain.Follower;
import com.example.momogum.domain.appointment.AppointmentInvitation;
import com.example.momogum.web.dto.appointment.AppointmentInviteDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentInviteRepository extends JpaRepository<AppointmentInvitation, Long> {

}
