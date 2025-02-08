package com.example.momogum.service.appointmentService;
import com.example.momogum.converter.appointmentConverter.AppointmentNameConverter;
import com.example.momogum.domain.appointment.AppointmentName;
import com.example.momogum.domain.utils.JwtUtil;
import com.example.momogum.repository.appoinmentRepo.AppointmentNameRepository;
import com.example.momogum.web.dto.appointment.AppointmentNameDTO.AppointmentNameResponseDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AppointmentNameServiceTest {

    @MockBean
    private JwtUtil jwtUtil;

    @Mock
    private AppointmentNameRepository repository; // Repository Mock

    @Mock
    private AppointmentNameConverter converter; // Converter Mock

    @InjectMocks
    private AppointmentNameService service; // 테스트 대상(Service)

    @Test
    void Success_NameTest() {

        //given
        AppointmentNameResponseDTO appointmentNameDTO = AppointmentNameResponseDTO.builder()
                .name("더술 출발")
                .menu("더술 닭 한마리")
                .date(LocalDateTime.of(2025, 1, 24, 18, 0))
                .location("중앙동 다이소 앞")
                .notes("꾸밈단계 2단계")
                .build();

        AppointmentName appointmentName = AppointmentName.builder()
                .id(1L)
                .name("더술 출발")
                .menu("더술 닭 한마리")
                .date(LocalDateTime.of(2025, 1, 24, 18, 0))
                .location("중앙동 다이소 앞")
                .notes("꾸밈단계 2단계")
                .build();

        when(converter.convert(appointmentNameDTO)).thenReturn(appointmentName);
        when(repository.save(appointmentName)).thenReturn(appointmentName);

        //when
        Long appointmnetNameId = service.creatAppointmentName(appointmentNameDTO);

        //then
        assertNotNull(appointmnetNameId);
        assertEquals(appointmnetNameId, 1L);

        verify(converter, times(1)).convert(appointmentNameDTO);
        verify(repository, times(1)).save(appointmentName);
    }

    @Test
    void Fail_NameTest() {

        //given
        AppointmentNameResponseDTO appointmentNameDTO = AppointmentNameResponseDTO.builder()
                .name("더술 출발")
                .menu("더술 닭 한마리")
                .date(LocalDateTime.of(2025, 1, 24, 18, 0))
                .location("중앙동 다이소 앞")
                .notes("꾸밈단계 2단계")
                .build();

        AppointmentName appointmentName = AppointmentName.builder()
                .name("더술 출발")
                .menu("더술 닭 한마리")
                .date(LocalDateTime.of(2025, 1, 24, 18, 0))
                .location("중앙동 다이소 앞")
                .notes("꾸밈단계 2단계")
                .build();

        // converter -  성공, Repository - 실패
        when(converter.convert(any())).thenReturn(appointmentName);
        when(repository.save(any())).thenThrow(new RuntimeException("DB를 찾을 수 없습니다."));

        // when
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.creatAppointmentName(appointmentNameDTO);
        });

        // 예외 메시지 확인
        assertEquals("DB를 찾을 수 없습니다.", exception.getMessage());
    }

}