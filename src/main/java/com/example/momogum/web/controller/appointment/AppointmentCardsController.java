package com.example.momogum.web.controller.appointment;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.service.appointmentService.AppointmentCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.momogum.web.dto.appointment.AppointmentCardDTO.*;

@RestController
@RequestMapping("/appointment")
@RequiredArgsConstructor
@Tag(name = "약속잡기 카드 선택 및 반환 API")
public class AppointmentCardsController {

    private final AppointmentCardService cardService;

    /**
     * 기본 카드 반환하는 메서드
     */
    @Operation(summary = "기본 카드 반환 API",
            description = "AWS S3에 저장되어 있는 기본 카드 목록을 반환합니다.")
    @GetMapping("/card/basic")
    public ApiResponse<List<AppointmentCardResponseDTO>> getBasicCards() {
        List<AppointmentCardResponseDTO> basicCards = cardService.getBasicCards();
        return ApiResponse.onSuccess(basicCards);
    }

    /**
     * 기본 카드 반환하는 메서드
     */
    @Operation(summary = "재미 카드 반환 API",
            description = "AWS S3에 저장되어 있는 재미 카드 목록을 반환합니다.")
    @GetMapping("/card/fun")
    public ApiResponse<List<AppointmentCardResponseDTO>> getFunCards() {
        List<AppointmentCardResponseDTO> funCards = cardService.getFunCards();
        return ApiResponse.onSuccess(funCards);
    }

}
