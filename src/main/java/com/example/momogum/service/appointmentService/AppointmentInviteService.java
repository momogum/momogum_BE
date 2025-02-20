package com.example.momogum.service.appointmentService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.GeneralException;
import com.example.momogum.apiPayLoad.exception.handler.UserEntityHandler;
import com.example.momogum.converter.appointmentConverter.AppointmentInviteConverter;
import com.example.momogum.domain.Follower;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.appointment.Appointment;
import com.example.momogum.domain.appointment.AppointmentInvitation;
import com.example.momogum.domain.common.enums.InvitationStatus;
import com.example.momogum.repository.appoinmentRepo.AppointmentInviteRepository;
import com.example.momogum.repository.appoinmentRepo.AppointmentRepository;
import com.example.momogum.repository.followRepo.FollowerRepository;
import com.example.momogum.repository.followRepo.FollowingRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.appointment.AppointmentInviteDTO.AppointmentInviteRequestDTO;
import com.example.momogum.web.dto.appointment.AppointmentInviteDTO.AppointmentInviteResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentInviteService {

    private final UserEntityRepository userEntityRepository;
    private final AppointmentInviteRepository appointmentInviteRepository;
    private final AppointmentInviteConverter converter;
    private final FollowerRepository followerRepository;
    private final AppointmentRepository appointmentRepository;


    /**
     * GET 약속에 초대 가능한 친구 목록 반환 (나를 팔로워하는 사람들)
     * @return List<AppointmentInviteResponseDTO> 객체
     */
    @Transactional(readOnly = true)
    public List<AppointmentInviteResponseDTO> getFriendsForInvitation(Long appointmentId, Long userId) {
        // 1. 나를 팔로우하는 사용자 조회 (팔로워)
        List<UserEntity> followers = followerRepository.findByUserId(userId)
                .stream()
                .map(Follower::getFollower)
                .toList();

        // 2. 이미 초대된 사용자 조회
        List<Long> invitedUserIds = appointmentInviteRepository.findByAppointmentId(appointmentId)
                .stream()
                .map(invitation -> invitation.getUserEntity().getId())
                .toList();

        // 3. 초대 가능한 사용자 필터링 (이미 초대된 사용자는 제외)
        List<UserEntity> availableUsers = followers.stream()
                .filter(user -> !invitedUserIds.contains(user.getId())) // 초대되지 않은 유저만 필터링
                .toList();

        // 4. DTO 변환 후 반환
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

        // 1. 초대할 사용자 조회 (DB에서 존재하는 userIds만 가져오기)
        List<UserEntity> users = userEntityRepository.findByIdIn(request.getUserIds());

        if (users.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, UserEntity> userMap = users.stream()
                .collect(Collectors.toMap(UserEntity::getId, Function.identity()));

        // 2. 초대할 약속 조회
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.APPOINTMENT_NOT_EXIST));

        // 3. 이미 초대된 사용자 ID 목록 조회 (한 번의 쿼리로 조회하여 contains()로 필터링)
        Set<Long> alreadyInvitedUserIds = new HashSet<>(appointmentInviteRepository.findByAppointmentId(request.getAppointmentId())
                .stream()
                .map(invitation -> invitation.getUserEntity().getId())
                .toList());

        List<AppointmentInvitation> invitationsToSave = new ArrayList<>();
        List<AppointmentInviteResponseDTO> invitedUsers = new ArrayList<>();

        // 4️⃣ 초대할 사용자 필터링 후 추가
        for (Long userId : request.getUserIds()) {
            UserEntity user = userMap.get(userId);

            if (user == null) {
                continue;
            }

            if (alreadyInvitedUserIds.contains(userId)) {
                continue;
            }

            // 5️⃣ 초대 객체 생성 후 리스트 추가
            AppointmentInvitation invitation = AppointmentInvitation.builder()
                    .appointment(appointment)
                    .userEntity(user)
                    .status(InvitationStatus.PENDING)
                    .build();

            invitationsToSave.add(invitation);
            invitedUsers.add(converter.toResponseDTO(user, InvitationStatus.PENDING));
        }

        // 6️⃣ 실제 DB 저장 (빈 리스트가 아닐 경우에만 실행)
        if (!invitationsToSave.isEmpty()) {
            appointmentInviteRepository.saveAll(invitationsToSave);
        }

        return invitedUsers;
    }




    private static void validateAppointmentInviteRequest(AppointmentInviteRequestDTO request) {
        if (request.getUserIds() == null || request.getUserIds().isEmpty()) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        if (request.getAppointmentId() == null) {
            throw new GeneralException(ErrorStatus.APPOINTMENT_NOT_EXIST);
        }
    }

    /**
     * 초대 상태 업데이트
     */
    public void updateInvitationStatus(Long appointmentId, InvitationStatus updateStatus) {
        List<AppointmentInvitation> invitations = appointmentInviteRepository.findByAppointmentId(appointmentId);

        invitations.forEach(invitation -> invitation.updateStatus(updateStatus));

        appointmentInviteRepository.saveAll(invitations);
    }
}