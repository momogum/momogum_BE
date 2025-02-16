package com.example.momogum.web.controller.appointment;

import com.example.momogum.converter.appointmentConverter.AppointmentConverter;
import com.example.momogum.domain.appointment.Appointment;
import com.example.momogum.domain.common.enums.CardCategory;
import com.example.momogum.domain.common.enums.InvitationStatus;
import com.example.momogum.domain.utils.JwtUtil;
import com.example.momogum.service.appointmentService.AppointmentCardService;
import com.example.momogum.service.appointmentService.AppointmentInviteService;
import com.example.momogum.service.appointmentService.AppointmentNameService;
import com.example.momogum.service.appointmentService.AppointmentService;
import com.example.momogum.service.appointmentService.orchestrator.AppointmentOrchestrator;
import com.example.momogum.web.dto.appointment.AppointmentCardDTO.AppointmentCardResponseDTO;
import com.example.momogum.web.dto.appointment.AppointmentInviteDTO.AppointmentInviteRequestDTO;
import com.example.momogum.web.dto.appointment.AppointmentInviteDTO.AppointmentInviteResponseDTO;
import com.example.momogum.web.dto.appointment.AppointmentNameDTO.AppointmentNameRequestDTO;
import com.example.momogum.web.dto.appointment.AppointmentOrchestratorDTO.AppointmentOrchestratorRequestDTO;
import com.example.momogum.web.dto.appointment.AppointmentOrchestratorDTO.AppointmentOrchestratorResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AppointmentOrchestratorTest {

    @MockBean
    private JwtUtil jwtUtil;


    @InjectMocks
    private AppointmentOrchestrator orchestrator;

    @Mock
    private AppointmentCardService cardService;

    @Mock
    private AppointmentInviteService inviteService;

    @Mock
    private AppointmentNameService nameService;

    @Mock
    private AppointmentConverter appointmentConverter;

    @Mock
    private AppointmentService appointmentService;


    private AppointmentOrchestratorRequestDTO request;
    private AppointmentInviteRequestDTO inviteRequest;
    private List<AppointmentInviteResponseDTO> invitedFriends;
    private CardCategory category;
    private List<AppointmentCardResponseDTO> selectedCards;
    private Appointment appointment;



    @BeforeEach
    void setUp() {

        //1. 공통 요소
        List<String> nicknames = List.of("user1", "user2");
        LocalDateTime time = LocalDateTime.of(2025, 2, 14, 15, 37);

        //2. 전체 요청 DTO
        request = AppointmentOrchestratorRequestDTO.builder()
                .userId(1L)
                .appointmentId(1L)
                .nicknames(nicknames)
                .cardCategory(CardCategory.BASIC)
                .appointmentName(
                        AppointmentNameRequestDTO.builder()
                                .name("약속 이름")
                                .menu("식사 메뉴")
                                .date(time)
                                .location("마라미방")
                                .notes("꾸밈 3단계")
                                .build()
                ).build();

        //3. 친구 초대 요청 DTO
        inviteRequest = AppointmentInviteRequestDTO.builder()
                .appointmentId(request.getAppointmentId())
                .nicknames(request.getNicknames())
                .build();

        //4. 친구 초대 서비스를 위한 데이터 생성
        invitedFriends = List.of(
                AppointmentInviteResponseDTO.builder()
                        .nickname("유저1")
                        .name("유저 이름1")
                        .profileImage("/path/to/image1.png")
                        .status(InvitationStatus.PENDING)
                        .build(),

                AppointmentInviteResponseDTO.builder()
                        .nickname("유저2")
                        .name("유저 이름2")
                        .profileImage("/path/to/image2.png")
                        .status(InvitationStatus.PENDING)
                        .build()
        );

        //5. 카드 조회 서비스를 위한 데이터 생성
        category = CardCategory.valueOf(request.getCardCategory().getCategory().toUpperCase());

        selectedCards = List.of(
                AppointmentCardResponseDTO.builder()
                        .category("basic")
                        .imageUrl("https://example-bucket.s3.amazonaws.com/basic/image1.jpg")
                        .build(),
                AppointmentCardResponseDTO.builder()
                        .category("fun")
                        .imageUrl("https://example-bucket.s3.amazonaws.com/fun/image1.jpg")
                        .build()
        );

        //6. Appointment 객체 생성
        appointment = Appointment.builder()
                .name("약속 이름")
                .menu("식사 메뉴")
                .date(time)
                .location("마라미방")
                .notes("꾸밈 3단계")
                .build();
    }

    @Test
    public void testCreateWholeAppointmentSuccess() {

        //Given
        when(appointmentConverter.toEntity(request)).thenReturn(appointment);

        when(inviteService.inviteFriends(any(AppointmentInviteRequestDTO.class))).thenReturn(invitedFriends);

        when(cardService.getCards(category)).thenReturn(selectedCards);

        doNothing().when(nameService).saveAppointmentName(request.getAppointmentName());

        when(appointmentService.save(any(Appointment.class))).thenReturn(appointment);

        when(appointmentConverter.toResponseDTO(appointment,selectedCards)).thenReturn(
                AppointmentOrchestratorResponseDTO.builder()
                        .appointmentId(1L)
                        .invitedFriends(invitedFriends)
                        .selectedCards(selectedCards)
                        .build()
        );

        //When
        AppointmentOrchestratorResponseDTO response = orchestrator.createWholeAppointment(request);

        //Then
        assertNotNull(response);
        assertEquals(1L, response.getAppointmentId());
        assertEquals(invitedFriends, response.getInvitedFriends());
        assertEquals(selectedCards, response.getSelectedCards());

        //verify
        verify(inviteService, times(1)).inviteFriends(inviteRequest);
        verify(cardService, times(1)).getCards(category);
        verify(nameService, times(1)).saveAppointmentName(request.getAppointmentName());
        verify(appointmentService, times(1)).save(appointment);
    }

}