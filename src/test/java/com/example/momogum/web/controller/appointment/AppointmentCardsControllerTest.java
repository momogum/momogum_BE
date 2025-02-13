package com.example.momogum.web.controller.appointment;

import com.example.momogum.domain.common.enums.CardCategory;
import com.example.momogum.domain.utils.JwtUtil;
import com.example.momogum.service.appointmentService.AppointmentCardService;
import com.example.momogum.web.dto.appointment.AppointmentCardDTO.AppointmentCardResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AppointmentCardsControllerTest {

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentCardService appointmentCardService;

    @BeforeEach
    void setUp() {
        when(appointmentCardService.getAllCards()).thenReturn(
                List.of(
                        new AppointmentCardResponseDTO("basic", "https://example-bucket.s3.amazonaws.com/basic/image1.jpg"),
                        new AppointmentCardResponseDTO("fun", "https://example-bucket.s3.amazonaws.com/fun/image2.jpg")
                )
        );

        when(appointmentCardService.getCards(CardCategory.BASIC)).thenReturn(
                List.of(
                        new AppointmentCardResponseDTO("basic", "https://example-bucket.s3.amazonaws.com/basic/image1.jpg"),
                        new AppointmentCardResponseDTO("basic", "https://example-bucket.s3.amazonaws.com/basic/image2.jpg")
                )
        );

        when(appointmentCardService.getCardsByCategory("basic")).thenReturn(
                List.of(
                        new AppointmentCardResponseDTO("basic", "https://example-bucket.s3.amazonaws.com/basic/image1.jpg"),
                        new AppointmentCardResponseDTO("basic", "https://example-bucket.s3.amazonaws.com/basic/image2.jpg")
                )
        );

        when(appointmentCardService.getCardsByCategory("fun")).thenReturn(
                List.of(
                        new AppointmentCardResponseDTO("fun", "https://example-bucket.s3.amazonaws.com/fun/image1.jpg"),
                        new AppointmentCardResponseDTO("fun", "https://example-bucket.s3.amazonaws.com/fun/image2.jpg")
                )
        );

        when(appointmentCardService.getCardsByCategory("event")).thenReturn(
                List.of(
                        new AppointmentCardResponseDTO("event", "https://example-bucket.s3.amazonaws.com/event/image1.jpg"),
                        new AppointmentCardResponseDTO("event", "https://example-bucket.s3.amazonaws.com/event/image2.jpg")
                )
        );
    }

    /**
     * 모든 카테고리 카드 반환 API 테스트
     */
    @Test
    void testGetAllCards() throws Exception {
        mockMvc.perform(get("/appointment/cards/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.message").value("성공입니다."))
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0].category").value("basic"))
                .andExpect(jsonPath("$.result[1].category").value("fun"));
    }

    /**
     * 특정 카테고리 카드 반환 API 테스트 (Enum 방식)
     */
    @Test
    void testGetCardsByCategory_Enum() throws Exception {
        mockMvc.perform(get("/appointment/cards/BASIC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.message").value("성공입니다."))
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0].category").value("basic"))
                .andExpect(jsonPath("$.result[0].imageUrl").value("https://example-bucket.s3.amazonaws.com/basic/image1.jpg"));
    }

    /**
     * 특정 카테고리 카드 반환 API 테스트 (QueryParam 방식)
     */
    @Test
    void testGetCardsByCategory_QueryParam() throws Exception {
        mockMvc.perform(get("/appointment/cards/category?category=fun"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.message").value("성공입니다."))
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0].category").value("fun"))
                .andExpect(jsonPath("$.result[0].imageUrl").value("https://example-bucket.s3.amazonaws.com/fun/image1.jpg"));
    }
}
