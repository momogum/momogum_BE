package com.example.momogum.service.appointmentService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.GeneralException;
import com.example.momogum.apiPayLoad.exception.handler.UserEntityHandler;
import com.example.momogum.converter.appointmentConverter.AppointmentInviteConverter;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.appointment.AppointmentInvitation;
import com.example.momogum.domain.common.enums.InvitationStatus;
import com.example.momogum.repository.appoinmentRepo.AppointmentInviteRepository;
import com.example.momogum.repository.followRepo.FollowingRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.appointment.AppointmentInviteDTO.AppointmentInviteRequestDTO;
import com.example.momogum.web.dto.appointment.AppointmentInviteDTO.AppointmentInviteResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentInviteService {

    private final UserEntityRepository userEntityRepository;
    private final AppointmentInviteRepository appointmentInviteRepository;
    private final AppointmentInviteConverter converter;
    private final FollowingRepository followingRepository;

    /**
     * GET 약속에 초대 가능한 친구 목록 반환
     * @return List<AppointmentInviteResponseDTO> 객체
     */
    @Transactional(readOnly = true)
    public List<AppointmentInviteResponseDTO> getFriendsForInvitation(Long appointmentId, Long userId) {
        // 1. 현재 사용자가 팔로우 중인 사용자 조회
        List<UserEntity> followedUsers = followingRepository.findFollowedUsersByUserId(userId);

        // 2. 이미 초대된 사용자 조회
        List<AppointmentInvitation> existingInvitations = appointmentInviteRepository.findByAppointmentId(appointmentId);
        List<Long> invitedUserIds = existingInvitations.stream()
                .map(invitation -> invitation.getUserEntity().getId())
                .toList();

        // 3. 초대 가능한 사용자 필터링
        List<UserEntity> availableUsers = followedUsers.stream()
                .filter(user -> !invitedUserIds.contains(user.getId())) // 이미 초대된 사용자 제외
                .toList();

        // 4. DTO 변환
        return availableUsers.stream()
                .map(user -> converter.toResponseDTO(user, InvitationStatus.PENDING))
                .collect(Collectors.toList());
    }

    /**
     * POST 약속 잡기에 친구 초대
     * @return List<AppointmentInviteResponseDTO> 객체
     */
    @Transactional
    public List<AppointmentInviteResponseDTO> inviteFriends(AppointmentInviteRequestDTO request) {

        validateAppointmentInviteRequest(request);

        List<AppointmentInviteResponseDTO> invitedUsers = new ArrayList<>();

        for(String username : request.getNicknames()) {
            UserEntity user = userEntityRepository.findByNickname(username)
                    .orElseThrow(() -> new UserEntityHandler(ErrorStatus.MEMBER_NOT_FOUND));

            boolean isInvited = appointmentInviteRepository.existsByAppointmentIdAndUserEntity(request.getAppointmentId(), user);
            if (isInvited) {
                continue;
            }

            AppointmentInvitation invitation = AppointmentInvitation.builder()
                    .appointmentId(request.getAppointmentId())
                    .userEntity(user)
                    .status(InvitationStatus.PENDING)
                    .build();

            appointmentInviteRepository.save(invitation);

            invitedUsers.add(converter.toResponseDTO(user, InvitationStatus.PENDING));
        }

        return invitedUsers;
    }

    private static void validateAppointmentInviteRequest(AppointmentInviteRequestDTO request) {
        if (request.getNicknames() == null || request.getNicknames().isEmpty()) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        if (request.getAppointmentId() == null) {
            throw new GeneralException(ErrorStatus.APPOINTMENT_NOT_EXIST);
        }
    }
}
