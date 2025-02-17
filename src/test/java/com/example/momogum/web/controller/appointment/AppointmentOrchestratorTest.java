package com.example.momogum.web.controller.appointment;

import com.example.momogum.converter.appointmentConverter.AppointmentConverter;
import com.example.momogum.domain.appointment.Appointment;
import com.example.momogum.domain.appointment.AppointmentCard;
import com.example.momogum.domain.common.enums.CardCategory;
import com.example.momogum.domain.common.enums.InvitationStatus;
import com.example.momogum.domain.utils.JwtUtil;
import com.example.momogum.repository.appoinmentRepo.AppointmentRepository;
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
import java.util.Optional;

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

    @Mock
    private AppointmentRepository appointmentRepository;

    private AppointmentOrchestratorRequestDTO request;
    private AppointmentInviteRequestDTO inviteRequest;
    private List<AppointmentInviteResponseDTO> invitedFriends;
    private CardCategory category;
    private List<AppointmentCardResponseDTO> selectedCards;
    private Appointment appointment;

    @BeforeEach
    void setUp() {

        // 1. 공통 요소
        List<String> nicknames = List.of("user1", "user2");
        LocalDateTime date = LocalDateTime.of(2025, 2, 14, 15, 37);

        // 2. request 먼저 초기화
        request = AppointmentOrchestratorRequestDTO.builder()
                .userId(1L)
                .appointmentId(1L)
                .nicknames(nicknames)
                .selectedCardUrl("https://example-bucket.s3.amazonaws.com/basic/image1.jpg")
                .cardCategory(CardCategory.BASIC)
                .appointmentName(
                        AppointmentNameRequestDTO.builder()
                                .name("약속 이름")
                                .menu("식사 메뉴")
                                .date(date)
                                .location("마라미방")
                                .notes("꾸밈 3단계")
                                .build()
                ).build();

        // 3. appointment 객체 생성 (request 기반)
        appointment = Appointment.builder()
                .id(request.getAppointmentId()) // request에서 가져오기
                .name(request.getAppointmentName().getName())
                .menu(request.getAppointmentName().getMenu())
                .date(request.getAppointmentName().getDate())
                .location(request.getAppointmentName().getLocation())
                .notes(request.getAppointmentName().getNotes())
                .build();

        //  `appointmentConverter.toEntity(request)` 호출 시 `appointment` 반환
        when(appointmentConverter.toEntity(request)).thenReturn(appointment);

        //  findById() 호출 시 appointment 반환
        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));

        // `appointmentService.createEmptyAppointment()`가 호출될 때 실제 appointment 반환
        when(appointmentService.createEmptyAppointment()).thenReturn(appointment);

        // `appointmentService.save()`도 정상적으로 동작하도록 설정
        when(appointmentService.save(any(Appointment.class))).thenReturn(appointment);
        // 4. 친구 초대 요청 DTO
        inviteRequest = AppointmentInviteRequestDTO.builder()
                .appointmentId(request.getAppointmentId()) // request에서 가져오기
                .nicknames(request.getNicknames())
                .build();

        // 5. 초대된 친구 데이터
        invitedFriends = List.of(
                new AppointmentInviteResponseDTO("user1", "유저 이름1", "/path/to/image1.png", InvitationStatus.PENDING),
                new AppointmentInviteResponseDTO("user2", "유저 이름2", "/path/to/image2.png", InvitationStatus.PENDING)
        );

        // 6. 카드 데이터
        selectedCards = List.of(
                new AppointmentCardResponseDTO("basic", "https://example-bucket.s3.amazonaws.com/basic/image1.jpg"),
                new AppointmentCardResponseDTO("fun", "https://example-bucket.s3.amazonaws.com/fun/image1.jpg")
        );
    }

    @Test
    public void testCreateWholeAppointmentSuccess() {

        // Given
        when(appointmentConverter.toEntity(request)).thenReturn(appointment);

        when(inviteService.inviteFriends(any(AppointmentInviteRequestDTO.class))).thenReturn(invitedFriends);

        // 카드 선택 저장 (getCards() → saveSelectedCards()로 변경)
        when(cardService.saveSelectedCards(any(Appointment.class), anyString(), any(CardCategory.class)))
                .thenReturn(AppointmentCard.builder()
                        .appointment(appointment)
                        .imageUrl("https://example-bucket.s3.amazonaws.com/basic/image1.jpg")
                        .category(CardCategory.BASIC)
                        .build());

        doNothing().when(nameService).saveAppointmentName(request.getAppointmentName());

        when(appointmentService.save(any(Appointment.class))).thenReturn(appointment);

        // 응답 변환 로직 수정
        when(appointmentConverter.toResponseDTO(eq(appointment), anyList())).thenReturn(
                AppointmentOrchestratorResponseDTO.builder()
                        .appointmentId(appointment.getId())
                        .invitedFriends(invitedFriends)
                        .selectedCards(selectedCards)
                        .build()
        );

        // When
        AppointmentOrchestratorResponseDTO response = orchestrator.createWholeAppointment(request);

        // Then
        assertNotNull(response);
        assertEquals(appointment.getId(), response.getAppointmentId());
        assertEquals(invitedFriends, response.getInvitedFriends());
        assertEquals(selectedCards, response.getSelectedCards());

        // Verify
        verify(inviteService, times(1)).inviteFriends(any(AppointmentInviteRequestDTO.class));
        verify(cardService, times(1)).saveSelectedCards(any(Appointment.class), anyString(), any(CardCategory.class));
        verify(nameService, times(1)).saveAppointmentName(request.getAppointmentName());
        verify(appointmentService, times(1)).save(any(Appointment.class));
    }
}
