package com.example.momogum.web.controller.appointment;

import com.example.momogum.service.appointment.AppointmentNameService;
import com.example.momogum.web.dto.appointment.AppointMentDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AppointmentNameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentNameService appointmentNameService;

    private static final String BASE_URL = "/Appointment";

    /**
     * 성공 테스트
     * @throws Exception
     */
    @Test
    void Success_NameTest() throws Exception {

        //given
        AppointMentDTO.AppointmentNameDTO appointmentNameDTO = AppointMentDTO.AppointmentNameDTO.builder()
                .name("더술 출발")
                .menu("더술 닭 한마리")
                .date(LocalDateTime.of(2025, 1, 24, 18, 0))
                .location("중앙동 다이소 앞")
                .notes("꾸밈단계 2단계")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        String jsonRequest = objectMapper.writeValueAsString(appointmentNameDTO);


        Long appointmentNameId = 1L;

        when(appointmentNameService.creatAppointmentName(Mockito.any()))
                .thenReturn(appointmentNameId);

        //Expected
        mockMvc.perform(post(BASE_URL + "/name")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))  // 수정된 경로
                .andExpect(jsonPath("$.message").value("성공입니다."));

    }

    /**
     * 실패 테스트
     * @throws Exception
     */
    @Test
    void Fail_NameTest() throws Exception {
        //given
        AppointMentDTO.AppointmentNameDTO appointmentNameDTO = AppointMentDTO.AppointmentNameDTO.builder()
                .name("")
                .menu("더술 닭 한마리")
                .date(LocalDateTime.of(2025, 1, 24, 18, 0))
                .location("중앙동 다이소 앞")
                .notes("꾸밈단계 2단계")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        String jsonRequest = objectMapper.writeValueAsString(appointmentNameDTO);

        //Expected
        mockMvc.perform(post(BASE_URL + "/name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest()) // 상태 코드 400 검증
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."));

        // 실패시 service 호출 X
        Mockito.verify(appointmentNameService, Mockito.times(0))
                .creatAppointmentName(Mockito.any());
    }



}