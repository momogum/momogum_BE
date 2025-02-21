package com.example.momogum.converter.appointmentConverter;

import com.example.momogum.domain.ProfileImage;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.appointment.Appointment;
import com.example.momogum.domain.appointment.AppointmentCard;
import com.example.momogum.domain.appointment.AppointmentInvitation;
import com.example.momogum.domain.common.enums.InvitationStatus;
import com.example.momogum.web.dto.appointment.AppointmentCardDTO;
import com.example.momogum.web.dto.appointment.AppointmentCardDTO.AppointmentCardRequestDTO;
import com.example.momogum.web.dto.appointment.AppointmentCardDTO.AppointmentCardResponseDTO;
import com.example.momogum.web.dto.appointment.AppointmentDTO.AppointmentDetailsDTO;
import com.example.momogum.web.dto.appointment.AppointmentDTO.AppointmentInfoDTO;
import com.example.momogum.web.dto.appointment.AppointmentDTO.AppointmentMainPageResponseDTO;
import com.example.momogum.web.dto.appointment.AppointmentInviteDTO.AppointmentInviteResponseDTO;
import com.example.momogum.web.dto.appointment.AppointmentOrchestratorDTO.AppointmentOrchestratorRequestDTO;
import com.example.momogum.web.dto.appointment.AppointmentOrchestratorDTO.AppointmentOrchestratorResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AppointmentConverter {

    /**
     * 임시 값 설정
     */
    public Appointment toTemporaryEntity(UserEntity sender) {
        return Appointment.builder()
                .name("치킨 한 번 뜯자")
                .menu("더 치킨")
                .date(LocalDateTime.now().plusDays(1))
                .location("한양대 에리카 앞")
                .notes("춥다 옷입고 와!")
                .sender(sender)
                .build();
    }

    /**
     * 실제 객체 -> Appointment 객체 생성
     */
    public Appointment toEntity(AppointmentOrchestratorRequestDTO request, UserEntity sender) {
        return   Appointment.builder()
                .name(request.getAppointmentName().getName())
                .menu(request.getAppointmentName().getMenu())
                .date(request.getAppointmentName().getDate())
                .location(request.getAppointmentName().getLocation())
                .notes(request.getAppointmentName().getNotes())
                .sender(sender)
                .build();
    }

    /**
     * 초대된 친구 목록 변환
     */
    private List<AppointmentInviteResponseDTO> convertInvitedFriends(List<AppointmentInvitation> invitations) {

        if(invitations == null || invitations.isEmpty()) {
            return createDummyInvitedFriends();
        }

        return Optional.ofNullable(invitations)
                .orElse(Collections.emptyList())
                .stream()
                .map(invite -> AppointmentInviteResponseDTO.builder()
                        .nickname(invite.getUserEntity().getNickname())
                        .name(invite.getUserEntity().getName())
                        .profileImage(Optional.ofNullable(invite.getUserEntity().getProfileImage())
                                .map(ProfileImage::getImageLink)
                                .orElse(null))
                        .status(invite.getStatus())
                        .build()).toList();
    }

    private List<AppointmentInviteResponseDTO> createDummyInvitedFriends() {
        return Arrays.asList(
                AppointmentInviteResponseDTO.builder()
                        .nickname("FrontHeadlock")
                        .name("덕규")
                        .profileImage("https://momogum-bucket.s3.ap-northeast-2.amazonaws.com/basic_profile/default_image.png")
                        .status(InvitationStatus.PENDING)
                        .build(),
                AppointmentInviteResponseDTO.builder()
                        .nickname("kut7228")
                        .name("쿠트")
                        .profileImage("https://momogum-bucket.s3.ap-northeast-2.amazonaws.com/basic_profile/default_image.png")
                        .status(InvitationStatus.PENDING)
                        .build());
    }


    /**
     * 오케스트레이터에서 사용되는 응답 DTO 변환
     */
    public AppointmentOrchestratorResponseDTO toResponseDTO(
            Appointment appointment, AppointmentCardResponseDTO selectedCard) {

        UserEntity sender = appointment.getSender();

        return AppointmentOrchestratorResponseDTO.builder()
                .name(appointment.getName())
                .menu(appointment.getMenu())
                .date(appointment.getDate() != null ? LocalDate.from(appointment.getDate()) : null)
                .location(appointment.getLocation())
                .notes(appointment.getNotes())
                .appointmentId(appointment.getId())
                .invitedFriends(convertInvitedFriends(appointment.getInvitations()))
                .selectedCard(selectedCard)
                .senderId(sender != null ? sender.getId() : null)
                .senderName(sender != null ? sender.getName() : "senderName이 존재하지 않습니다.")
                .status(InvitationStatus.ACCEPTED)
                .build();
    }

    /**
     * 메인페이지에서 사용되는 응답 DTO 변환
     */
    public AppointmentMainPageResponseDTO toMainPageDTO(
            Appointment appointment, List<AppointmentCardResponseDTO> selectedCards) {

        UserEntity sender = appointment.getSender();

        return AppointmentMainPageResponseDTO.builder()
                .name(appointment.getName())
                .menu(appointment.getMenu())
                .date(appointment.getDate() != null ? LocalDate.from(appointment.getDate()) : null)
                .location(appointment.getLocation())
                .notes(appointment.getNotes())
                .appointmentId(appointment.getId())
                .invitedFriends(convertInvitedFriends(appointment.getInvitations()))
                .selectedCards(selectedCards)
                .senderId(sender != null ? sender.getId() : null)
                .senderName(sender != null ? sender.getName() : "senderName이 존재하지 않습니다.")
                .status(InvitationStatus.ACCEPTED)
                .build();
    }

    /**
     * 초대장 조회를 위한 DTO 변환
     */
    public AppointmentDetailsDTO toDetailsDTO(Appointment appointment, List<AppointmentInvitation> invitations, AppointmentCard selectedCard) {

        return AppointmentDetailsDTO.builder()
                .appointmentId(appointment.getId())
                .appointmentInfo(AppointmentInfoDTO.builder()
                        .name(appointment.getName())
                        .menu(appointment.getMenu())
                        .date(appointment.getDate())
                        .location(appointment.getLocation())
                        .notes(appointment.getNotes())
                        .build())
                .selectedCard(AppointmentCardResponseDTO.builder()
                        .category(selectedCard.getCategory().getCategory())
                        .imageUrl(selectedCard.getImageUrl())
                        .build())
                .invitedFriends(convertInvitedFriends(invitations))
                .build();
    }

}