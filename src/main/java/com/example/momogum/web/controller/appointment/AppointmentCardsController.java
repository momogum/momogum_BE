package com.example.momogum.web.controller.appointment;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.domain.common.enums.CardCategory;
import com.example.momogum.service.appointmentService.AppointmentCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.momogum.web.dto.appointment.AppointmentCardDTO.*;

@RestController
@RequestMapping("/appointment/cards")
@RequiredArgsConstructor
@Tag(name = "약속잡기 카드 선택 및 반환 API")
public class AppointmentCardsController {

    private final AppointmentCardService cardService;

    @Operation(summary = "모든 카테고리 카드 반환 API",
            description = "AWS S3에 저장되어 있는 모든 카드를 반환합니다.")
    @GetMapping("/all")
    public ApiResponse<List<AppointmentCardResponseDTO>> getAllCards() {
        List<AppointmentCardResponseDTO> cards = cardService.getAllCards();
        return ApiResponse.onSuccess(cards);
    }

    @Operation(summary = "카드 반환 API (Enum 방식)",
            description = "AWS S3에 저장되어 있는 특정 카테고리 카드 목록을 반환합니다.")
    @GetMapping("/{category}")
    public ApiResponse<List<AppointmentCardResponseDTO>> getCards(@PathVariable CardCategory category) {
        List<AppointmentCardResponseDTO> cards = cardService.getCards(category);
        return ApiResponse.onSuccess(cards);
    }

    @Operation(summary = "카드 반환 API (Query Parameter 방식)",
            description = "AWS S3에 저장되어 있는 특정 카테고리 카드 목록을 반환합니다.")
    @GetMapping("/category")
    public ApiResponse<List<AppointmentCardResponseDTO>> getCards(@RequestParam String category) {
        List<AppointmentCardResponseDTO> cards = cardService.getCardsByCategory(category);
        return ApiResponse.onSuccess(cards);
    }
}
