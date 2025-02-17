package com.example.momogum.service.appointmentService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.GeneralException;
import com.example.momogum.converter.appointmentConverter.AppointmentInviteConverter;
import com.example.momogum.domain.Follower;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.appointment.Appointment;
import com.example.momogum.domain.appointment.AppointmentInvitation;
import com.example.momogum.domain.common.enums.InvitationStatus;
import com.example.momogum.domain.common.enums.LoginType;
import com.example.momogum.domain.utils.JwtUtil;
import com.example.momogum.repository.appoinmentRepo.AppointmentInviteRepository;
import com.example.momogum.repository.appoinmentRepo.AppointmentRepository;
import com.example.momogum.repository.followRepo.FollowerRepository;
import com.example.momogum.repository.followRepo.FollowingRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.appointment.AppointmentInviteDTO.AppointmentInviteRequestDTO;
import com.example.momogum.web.dto.appointment.AppointmentInviteDTO.AppointmentInviteResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AppointmentInviteServiceTest {


    @MockBean
    private JwtUtil jwtUtil;

    @InjectMocks
    private AppointmentInviteService appointmentInviteService;

    @Mock
    private UserEntityRepository userEntityRepository;

    @Mock
    private AppointmentInviteRepository appointmentInviteRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AppointmentInviteConverter converter;

    @Mock
    private FollowingRepository followingRepository;

    @Mock
    private FollowerRepository followerRepository;

    private AppointmentInviteRequestDTO request;
    private UserEntity user1;
    private UserEntity user2;
    private Appointment appointment;


    @BeforeEach
    void setUp() {

        appointment = Appointment.builder()
                .id(1L)
                .name("테스트 약속")
                .date(LocalDateTime.now().plusDays(1))
                .location("강남역")
                .build();

        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));

        // 테스트 요청 객체 생성 (appointmentId와 초대할 사용자 닉네임 목록)
        request = AppointmentInviteRequestDTO.builder()
                .appointmentId(1L)
                .nicknames(List.of("user1", "user2"))
                .build();

        // 테스트용 UserEntity 생성 (필요한 필드만 설정)
        user1 = createTestUser(1L, "user1", "유저 닉네임 1");
        user2 = createTestUser(2L, "user2", "유저 닉네임 2");

        // repository에서 username으로 UserEntity 조회 시 설정
        when(userEntityRepository.findByNicknameIn(List.of("user1", "user2")))
                .thenReturn(List.of(user1, user2));

        // converter의 DTO 변환 결과를 미리 설정 1
        when(converter.toResponseDTO(user1, InvitationStatus.PENDING))
                .thenReturn(new AppointmentInviteResponseDTO("user1", "유저 닉네임 1", "/path/to/image1", InvitationStatus.PENDING));

        when(converter.toResponseDTO(user2, InvitationStatus.PENDING))
                .thenReturn(new AppointmentInviteResponseDTO("user2", "유저 닉네임 2", "/path/to/image2", InvitationStatus.PENDING));
    }

    /**
     * 테스트용 UserEntity 객체를 생성하는 팩토리 메서드.
     */
    private UserEntity createTestUser(Long id, String username, String name) {
        return UserEntity.builder()
                .id(id)
                .nickname(username) //username이 nickname의 역할을 수행한다고 가정
                .name(name)
                .phoneNumber("010-1234-5678")
                .about("About " + name)
                .provider(LoginType.KAKAO)
                .build();
    }

    /**
     * 맞팔된 사용자만 초대 가능 테스트
     */
    @DisplayName("맞팔된 사용자만 초대 가능 - getFriendsForInvitation 테스트")
    @Test
    void getFriendsForInvitation_SuccessTest() {

        //Given - 특정 appointmentId에 대한 특정 초대 목록 생성
        Long currentUserId = 100L; //테스트용 유저 ID 생성

        // 현재 사용자가 팔로잉 하고 있는 목록 설정 (사용자 -> 팔로우 -> user1, user2)
        List<UserEntity> followingUsers = List.of(user1, user2);
        when(followingRepository.findFollowedUsersByUserId(currentUserId))
                .thenReturn(followingUsers);

        // 현재 사용자를 팔로우하는 목록 설정 (user1만 맞팔)
        when(followerRepository.findByUserId(currentUserId))
                .thenReturn(List.of(new Follower(1L, createTestUser(currentUserId, "currentUser", "현재 사용자"), user1)));

        // 이미 초대된 사용자 목록 (초대된 사람 없다고 가정)
        when(appointmentInviteRepository.findByAppointmentId(request.getAppointmentId()))
                .thenReturn(Collections.emptyList());

        //When
        List<AppointmentInviteResponseDTO> result = appointmentInviteService.getFriendsForInvitation(request.getAppointmentId(), currentUserId);

        //Then - user1만 조회되어야 함
        assertEquals(1, result.size());
        assertEquals("user1", result.get(0).getNickname());

    }

    /**
     * 초대 성공 리스트 - Batch Insert
     */
    @DisplayName("inviteFriends 테스트 - Batch Insert로 초대 저장")
    @Test
    void inviteFriends_SuccessTest() {
        // given - user1, user2 둘 다 초대가 되어있지 않은 상태
        when(appointmentInviteRepository.existsByAppointmentIdAndUserEntity(appointment.getId(), user1))
                .thenReturn(false);
        when(appointmentInviteRepository.existsByAppointmentIdAndUserEntity(appointment.getId(), user2))
                .thenReturn(false);

        // when
        List<AppointmentInviteResponseDTO> result = appointmentInviteService.inviteFriends(request);

        // then
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getNickname());
        assertEquals("user2", result.get(1).getNickname());

        // Batch Insert -> saveAll()이 1번 호출되어야 함
        verify(appointmentInviteRepository, times(1)).saveAll(anyList());
    }

    /**
     * 초대 상태 업데이트 테스트
     */
    @DisplayName("초대 상태 업데이트 테스트 (PENDING → ACCEPTED)")
    @Test
    void updateInvitationStatus_SuccessTest() {

        //Given - 기존 초대 목록을 'PENDING' 상태로 설정
        List<AppointmentInvitation> invitations = List.of(
                AppointmentInvitation.builder().appointment(appointment).userEntity(user1).status(InvitationStatus.PENDING).build(),
                AppointmentInvitation.builder().appointment(appointment).userEntity(user1).status(InvitationStatus.PENDING).build()
        );

        when(appointmentInviteRepository.findByAppointmentId(anyLong())).thenReturn(invitations);

        //When
        appointmentInviteService.updateInvitationStatus(appointment.getId(), InvitationStatus.ACCEPTED);

        //Then
        invitations.forEach(invite -> assertEquals(InvitationStatus.ACCEPTED, invite.getStatus()));

        //검증
        verify(appointmentInviteRepository, times(1)).saveAll(invitations);

    }


    @DisplayName("inviteFriends 테스트 - 이미 초대된 사용자는 제외")
    @Test
    void inviteFriends_DuplicateTest() {
        // given - user1은 초대 X, user2은 초대 O
        when(appointmentInviteRepository.existsByAppointmentIdAndUserEntity(appointment.getId(), user1))
                .thenReturn(false);
        when(appointmentInviteRepository.existsByAppointmentIdAndUserEntity(appointment.getId(), user2))
                .thenReturn(true);

        // when
        List<AppointmentInviteResponseDTO> result = appointmentInviteService.inviteFriends(request);

        // then
        assertEquals(1, result.size());
        assertEquals("user1", result.get(0).getNickname());

        verify(appointmentInviteRepository, times(1)).saveAll(anyList());
    }

    @DisplayName("inviteFriends 테스트 - 초대할 친구를 아무도 지정하지 않았을 경우 -> MEMBER_NOT_FOUND 오류가 발생해야 함.")
    @Test
    void inviteFriends_InvalidRequest_EmptyUsernamesTest() {
        //given - 초대할 친구를 아무도 지정하지 않았을 경우
        AppointmentInviteRequestDTO request = AppointmentInviteRequestDTO.builder()
                .appointmentId(1L)
                .nicknames(Collections.emptyList())
                .build();

        //when & then - 예상 기대값 : GeneralException - MEMBER_NOT_FOUND
        GeneralException exception = assertThrows(GeneralException.class,
                () -> appointmentInviteService.inviteFriends(request));
        assertEquals(ErrorStatus.MEMBER_NOT_FOUND, exception.getCode());
    }

    @DisplayName("inviteFriends 테스트 - appointmnetId가 null일 경우 약속 관련 오류 (APPOINTMENT_NOT_EXIST) 발생")
    @Test
    void inviteFriends_InvalidRequest_NullAppointmentIdTest() {
        //given - appointmentId == null
        AppointmentInviteRequestDTO invalidRequest = AppointmentInviteRequestDTO.builder()
                .appointmentId(null)
                .nicknames(List.of("user1"))
                .build();

        //when & then - 예상 기대값 : GeneralException - APPOINTMENT_NOT_EXIST
        GeneralException exception = assertThrows(GeneralException.class,
                () -> appointmentInviteService.inviteFriends(invalidRequest));
        assertEquals(ErrorStatus.APPOINTMENT_NOT_EXIST, exception.getCode());
    }
}