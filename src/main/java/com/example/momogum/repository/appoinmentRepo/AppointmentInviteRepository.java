package com.example.momogum.repository.appoinmentRepo;

import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.appointment.Appointment;
import com.example.momogum.domain.appointment.AppointmentInvitation;
import com.example.momogum.domain.common.enums.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AppointmentInviteRepository extends JpaRepository<AppointmentInvitation, Long> {

    boolean existsByAppointmentIdAndUserEntity(Long appointmentId, UserEntity user);

    List<AppointmentInvitation> findByAppointmentId(Long appointmentId);

    @Query("SELECT ai.appointment FROM AppointmentInvitation ai " +
            "WHERE ai.userEntity.id = :userId " +
            "AND ai.status = :status " +
            "ORDER BY ai.appointment.date ASC")
    List<Appointment> findAppointmentsByStatus(@Param("userId") Long userId, @Param("status") InvitationStatus status);
}
