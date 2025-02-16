package com.example.momogum.domain.appointment;

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

    //초대 친구 추가 연관관계 메서드
    public void addInvitation(AppointmentInvitation invitation) {
        invitations.add(invitation);
        invitation.setAppointment(this);
    }

    //카드 추가 메서드
    public void addCard(AppointmentCard card) {
        selectedCards.add(card);
        card.setAppointment(this);
    }


}
