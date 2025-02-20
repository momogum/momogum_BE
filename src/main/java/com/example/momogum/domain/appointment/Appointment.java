package com.example.momogum.domain.appointment;

import com.example.momogum.domain.UserEntity;
import com.example.momogum.web.dto.appointment.AppointmentOrchestratorDTO;
import com.example.momogum.web.dto.appointment.AppointmentOrchestratorDTO.AppointmentOrchestratorRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "appointment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String menu;
    private LocalDateTime date;
    private String location;
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private UserEntity sender;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AppointmentInvitation> invitations = new ArrayList<>();

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AppointmentCard> selectedCards = new ArrayList<>();

    public Appointment updateAppointment(AppointmentOrchestratorRequestDTO request) {
        return this.toBuilder()
                .name(request.getAppointmentName().getName())
                .menu(request.getAppointmentName().getMenu())
                .date(request.getAppointmentName().getDate())
                .location(request.getAppointmentName().getLocation())
                .notes(request.getAppointmentName().getNotes())
                .build();
    }
}
