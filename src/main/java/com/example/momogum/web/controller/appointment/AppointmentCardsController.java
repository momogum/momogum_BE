package com.example.momogum.web.controller.appointment;

import com.example.momogum.service.appointment.AppointmentCardService;
import com.example.momogum.web.dto.appointment.AppointMentDTO.AppointmentCardResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Appointment")
@RequiredArgsConstructor
public class AppointmentCardsController {

    private final AppointmentCardService cardService;

    /**
     * 기본 카드 반환하는 메서드
     */
    @GetMapping("/card/basic")
    public List<AppointmentCardResponseDTO> getBasicCards() {
        return cardService.getBasicCards();
    }

    /**
     * 기본 카드 반환하는 메서드
     */
    @GetMapping("/card/fun")
    public List<AppointmentCardResponseDTO> getFunCards() {
        return cardService.getFunCards();
    }

}
