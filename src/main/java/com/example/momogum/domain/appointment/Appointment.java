package com.example.momogum.domain.appointment;

import com.example.momogum.web.dto.appointment.AppointmentNameDTO;
import com.example.momogum.web.dto.appointment.AppointmentNameDTO.AppointmentNameRequestDTO;
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
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String menu;
    private LocalDateTime date;
    private String location;
    private String notes;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AppointmentInvitation> invitations = new ArrayList<>();

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AppointmentCard> selectedCards = new ArrayList<>();

    // Appointment 업데이트 메서드
    public void updateAppointmentDetails(AppointmentNameRequestDTO request) {
        this.name = request.getName();
        this.menu = request.getMenu();
        this.date = request.getDate();
        this.location = request.getLocation();
        this.notes = request.getNotes();
    }

}
