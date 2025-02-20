package com.example.momogum.web.controller.appointment;

import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.appointment.Appointment;
import com.example.momogum.domain.utils.JwtUtil;
import com.example.momogum.repository.appoinmentRepo.AppointmentRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.service.appointmentService.AppointmentService;
import com.example.momogum.util.FirebaseCloudMessageUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AppointmentInitControllerTest {

    @MockBean
    private FirebaseCloudMessageUtil util;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private AppointmentRepository appointmentRepository;

    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private UserEntityRepository userEntityRepository; // 추가

    @Test
    @DisplayName("약속 초기화 API 성공 테스트")
    void testInitAppointmentSuccess() throws Exception {
        // given
        Appointment mockAppointment = new Appointment();
        ReflectionTestUtils.setField(mockAppointment, "id", 1L);

        // 더미 사용자 생성
        UserEntity mockUser = new UserEntity();
        ReflectionTestUtils.setField(mockUser, "id", 1L);

        // creator 설정
        mockAppointment.setSender(mockUser);

        when(appointmentService.createTemporaryAppointment()).thenReturn(mockAppointment);

        // when & then
        mockMvc.perform(post("/appointment/init")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.appointmentId").value(1L))
                .andDo(print());

        verify(appointmentService, times(1)).createTemporaryAppointment();
    }
}
