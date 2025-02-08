package com.example.momogum.web.controller.appointment;

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
        // 공통적으로 사용할 Mock 데이터를 미리 정의
        when(appointmentCardService.getBasicCards()).thenReturn(
                List.of(
                        new AppointmentCardResponseDTO("basic", "https://example-bucket.s3.amazonaws.com/basic/image1.jpg"),
                        new AppointmentCardResponseDTO("basic", "https://example-bucket.s3.amazonaws.com/basic/image2.jpg")
                )
        );

        when(appointmentCardService.getFunCards()).thenReturn(
                List.of(
                        new AppointmentCardResponseDTO("fun", "https://example-bucket.s3.amazonaws.com/fun/image1.jpg"),
                        new AppointmentCardResponseDTO("fun", "https://example-bucket.s3.amazonaws.com/fun/image2.jpg")
                )
        );
    }

    @Test
    void testGetBasicCards() throws Exception {
        mockMvc.perform(get("/appointment/card/basic"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.message").value("성공입니다."))
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0].type").value("basic"))
                .andExpect(jsonPath("$.result[0].imageUrl").value("https://example-bucket.s3.amazonaws.com/basic/image1.jpg"))
                .andExpect(jsonPath("$.result[1].type").value("basic"))
                .andExpect(jsonPath("$.result[1].imageUrl").value("https://example-bucket.s3.amazonaws.com/basic/image2.jpg"));
    }


    @Test
    void testGetFunCards() throws Exception {
        mockMvc.perform(get("/appointment/card/fun"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.message").value("성공입니다."))
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0].type").value("fun"))
                .andExpect(jsonPath("$.result[0].imageUrl").value("https://example-bucket.s3.amazonaws.com/fun/image1.jpg"))
                .andExpect(jsonPath("$.result[1].type").value("fun"))
                .andExpect(jsonPath("$.result[1].imageUrl").value("https://example-bucket.s3.amazonaws.com/fun/image2.jpg"));
    }
}