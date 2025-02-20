package com.example.momogum.web.controller.appointment;

import com.example.momogum.converter.appointmentConverter.AppointmentConverter;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.appointment.Appointment;
import com.example.momogum.domain.appointment.AppointmentCard;
import com.example.momogum.domain.common.enums.CardCategory;
import com.example.momogum.domain.common.enums.InvitationStatus;
import com.example.momogum.domain.utils.JwtUtil;
import com.example.momogum.repository.appoinmentRepo.AppointmentRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.service.appointmentService.AppointmentCardService;
import com.example.momogum.service.appointmentService.AppointmentInviteService;
import com.example.momogum.service.appointmentService.AppointmentNameService;
import com.example.momogum.service.appointmentService.AppointmentService;
import com.example.momogum.service.appointmentService.orchestrator.AppointmentOrchestrator;
import com.example.momogum.util.FirebaseCloudMessageUtil;
import com.example.momogum.web.dto.appointment.AppointmentCardDTO;
import com.example.momogum.web.dto.appointment.AppointmentCardDTO.AppointmentCardRequestDTO;
import com.example.momogum.web.dto.appointment.AppointmentCardDTO.AppointmentCardResponseDTO;
import com.example.momogum.web.dto.appointment.AppointmentInviteDTO.AppointmentInviteRequestDTO;
import com.example.momogum.web.dto.appointment.AppointmentInviteDTO.AppointmentInviteResponseDTO;
import com.example.momogum.web.dto.appointment.AppointmentNameDTO.AppointmentNameRequestDTO;
import com.example.momogum.web.dto.appointment.AppointmentOrchestratorDTO.AppointmentOrchestratorRequestDTO;
import com.example.momogum.web.dto.appointment.AppointmentOrchestratorDTO.AppointmentOrchestratorResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

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
    private FirebaseCloudMessageUtil util;

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

    @Mock
    private UserEntityRepository userEntityRepository;


    private AppointmentOrchestratorRequestDTO request;
    private List<AppointmentInviteResponseDTO> invitedFriends;
    private AppointmentCardResponseDTO selectedCard;
    private Appointment appointment;
    private UserEntity sender;

    @BeforeEach
    void setUp() {

        // 1. 초대한 사람 (더미 데이터)
        sender = new UserEntity();
        ReflectionTestUtils.setField(sender, "id", 9L);
        ReflectionTestUtils.setField(sender, "name", "머머금");

        // 2. 공통 데이터 설정
        List<String> nicknames = List.of("user1", "user2");
        LocalDateTime date = LocalDateTime.of(2025, 2, 20, 18, 26);

        // 3. 요청 DTO 생성
        request = AppointmentOrchestratorRequestDTO.builder()
                .userId(sender.getId())
                .appointmentId(9L)
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

        // 4. Appointment 엔티티 생성
        appointment = Appointment.builder()
                .id(request.getAppointmentId())
                .name(request.getAppointmentName().getName())
                .menu(request.getAppointmentName().getMenu())
                .date(request.getAppointmentName().getDate())
                .location(request.getAppointmentName().getLocation())
                .notes(request.getAppointmentName().getNotes())
                .sender(sender)
                .build();

        // 5. 초대된 친구 리스트
        invitedFriends = List.of(
                new AppointmentInviteResponseDTO("user1", "유저 이름1", "/path/to/image1.png", InvitationStatus.PENDING),
                new AppointmentInviteResponseDTO("user2", "유저 이름2", "/path/to/image2.png", InvitationStatus.PENDING)
        );

        // 6. 카드 정보 (단일 카드 선택)
        selectedCard = new AppointmentCardResponseDTO("basic", "https://example-bucket.s3.amazonaws.com/basic/image1.jpg");

        when(cardService.selectCard(anyLong(), any(AppointmentCardRequestDTO.class)))
                .thenReturn(selectedCard);

        doNothing().when(nameService).saveAppointmentName(request.getAppointmentName());

        // 7. Mock 설정
        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(sender));
        when(appointmentConverter.toEntity(request, sender)).thenReturn(appointment);
        when(appointmentService.createTemporaryAppointment()).thenReturn(appointment);
        when(appointmentService.save(any(Appointment.class))).thenReturn(appointment);
        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));

        // 8. 응답 데이터
        when(appointmentConverter.toResponseDTO(eq(appointment), any(AppointmentCardResponseDTO.class)))
                .thenReturn(AppointmentOrchestratorResponseDTO.builder()
                        .appointmentId(appointment.getId())
                        .invitedFriends(invitedFriends)
                        .selectedCard(selectedCard)
                        .senderId(sender.getId())
                        .senderName(sender.getName())
                        .build());
    }

    @Test
    @DisplayName("약속 생성 성공 테스트")
    public void testCreateWholeAppointmentSuccess() {

        // When
        AppointmentOrchestratorResponseDTO response = orchestrator.createWholeAppointment(request);

        // Then
        assertNotNull(response);
        assertEquals(appointment.getId(), response.getAppointmentId());
        assertEquals(invitedFriends, response.getInvitedFriends());
        assertEquals(selectedCard, response.getSelectedCard());
        assertEquals(sender.getId(), response.getSenderId());
        assertEquals(sender.getName(), response.getSenderName());

        // Verify
        verify(inviteService, times(1)).inviteFriends(any(AppointmentInviteRequestDTO.class));
        verify(cardService, times(1)).selectCard(anyLong(), any(AppointmentCardRequestDTO.class));
    }
}
