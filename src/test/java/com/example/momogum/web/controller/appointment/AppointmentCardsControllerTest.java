package com.example.momogum.web.controller.appointment;

import com.example.momogum.service.appointment.AppointmentCardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.example.momogum.web.dto.appointment.AppointMentDTO.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AppointmentCardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentCardService appointmentCardService;

    @Test
    void testGetBasicCards() throws Exception {
        // Mock 서비스 응답
        when(appointmentCardService.getBasicCards()).thenReturn(
                List.of(
                        new AppointmentCardResponseDTO("basic", "https://example-bucket.s3.amazonaws.com/basic/image1.jpg"),
                        new AppointmentCardResponseDTO("basic", "https://example-bucket.s3.amazonaws.com/basic/image2.jpg")
                )
        );

        // API 호출 및 검증
        mockMvc.perform(get("/Appointment/card/basic"))
                .andExpect(status().isOk())
                .andExpect( MockMvcResultMatchers.content().json("""
                    {
                        "success": true,
                        "message": "요청이 성공적으로 처리되었습니다.",
                        "data": [
                            {"type":"basic", "imageUrl":"https://example-bucket.s3.amazonaws.com/basic/image1.jpg"},
                            {"type":"basic", "imageUrl":"https://example-bucket.s3.amazonaws.com/basic/image2.jpg"}
                        ]
                    }
                """));
    }


    @Test
    void testGetFunCards() throws Exception {
        // Mock 서비스 응답
        when(appointmentCardService.getFunCards()).thenReturn(
                List.of(
                        new AppointmentCardResponseDTO("fun", "https://example-bucket.s3.amazonaws.com/fun/image1.jpg"),
                        new AppointmentCardResponseDTO("fun", "https://example-bucket.s3.amazonaws.com/fun/image2.jpg")
                )
        );

        // API 호출 및 검증
        mockMvc.perform(get("/Appointment/card/fun"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                    {
                        "success": true,
                        "message": "요청이 성공적으로 처리되었습니다.",
                        "data": [
                            {"type":"fun", "imageUrl":"https://example-bucket.s3.amazonaws.com/fun/image1.jpg"},
                            {"type":"fun", "imageUrl":"https://example-bucket.s3.amazonaws.com/fun/image2.jpg"}
                        ]
                    }
                """));
    }

}