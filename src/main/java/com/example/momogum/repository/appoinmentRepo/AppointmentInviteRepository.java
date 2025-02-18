package com.example.momogum.repository.appoinmentRepo;

import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.appointment.AppointmentInvitation;
import com.example.momogum.web.dto.appointment.AppointmentDTO.AcceptedInvitationDTO;
import com.example.momogum.web.dto.appointment.AppointmentDTO.PendingInvitationDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentInviteRepository extends JpaRepository<AppointmentInvitation, Long> {

    boolean existsByAppointmentIdAndUserEntity(Long appointmentId, UserEntity user);

    List<AppointmentInvitation> findByAppointmentId(Long appointmentId);

    @Query("SELECT a.id as appointmentId, a.date as date, a.name as name, creator.nickname as creatorNickname " +
            "FROM AppointmentInvitation ai " +
            "JOIN ai.appointment a " +
            "JOIN ai.userEntity invitedUser " +
            "JOIN a.creator creator " +
            "WHERE invitedUser.id = :userId " +
            "AND ai.status = 'PENDING'"
    )
    List<PendingInvitationDTO> findPendingInvitations(Long userId);


    @Query("SELECT a.id as appointmentId, a.date as date, a.location as location, a.name as appointmentName, a.menu as menu " +
            "FROM AppointmentInvitation ai " +
            "JOIN ai.appointment a " +
            "WHERE ai.userEntity.id = :userId " +
            "AND ai.status = 'ACCEPTED' " +
            "ORDER BY a.date ASC")
    List<AcceptedInvitationDTO> findAcceptedAppointments(Long userId);

}
