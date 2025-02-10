package com.example.momogum.service.appointmentService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.GeneralException;
import com.example.momogum.apiPayLoad.exception.handler.UserEntityHandler;
import com.example.momogum.converter.appointmentConverter.AppointmentInviteConverter;
import com.example.momogum.domain.Follower;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.appointment.AppointmentInvitation;
import com.example.momogum.domain.common.enums.InvitationStatus;
import com.example.momogum.repository.appoinmentRepo.AppointmentInviteRepository;
import com.example.momogum.repository.followRepo.FollowerRepository;
import com.example.momogum.repository.followRepo.FollowingRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.appointment.AppointmentInviteDTO.AppointmentInviteRequestDTO;
import com.example.momogum.web.dto.appointment.AppointmentInviteDTO.AppointmentInviteResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentInviteService {

    private final UserEntityRepository userEntityRepository;
    private final AppointmentInviteRepository appointmentInviteRepository;
    private final AppointmentInviteConverter converter;
    private final FollowerRepository followerRepository;
    private final FollowingRepository followingRepository;


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

        // 2. 내가 팔로우하는 사용자 조회 (팔로잉)
        List<UserEntity> following = followingRepository.findFollowedUsersByUserId(userId);

        // 3. 맞팔 사용자만 가능하도록 필터링
        // 3-1) 내가 팔로우 한 사용자의 ID를 조회 후
        Set<Long> followingIds = following.stream()
                .map(UserEntity::getId)
                .collect(Collectors.toSet());

        // 3-2) followers에서 필터링하여 맞팔 되어 있는 사용자(mutualFollowers)만 선택
        List<UserEntity> mutualFollowers = followers.stream()
                .filter(user -> followingIds.contains(user.getId()))
                .toList();

        // 4. 이미 초대된 사용자 조회 (이 부분 유지)
        List<Long> invitedUserIds = appointmentInviteRepository.findByAppointmentId(appointmentId)
                .stream()
                .map(invitation -> invitation.getUserEntity().getId())
                .toList();

        // 5. 초대 가능한 사용자 필터링 (이미 초대된 사용자는 제외)
        List<UserEntity> avaliableUsers = mutualFollowers.stream()
                .filter(user -> !invitedUserIds.contains(user.getId()))
                .toList();

        // 6. DTO 변환 후 반환
        return avaliableUsers.stream()
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

        //1. 체크한 모든 사용자 조회 (Batch 조회)
        List<UserEntity> users = userEntityRepository.findByNicknameIn(request.getNicknames());

        //2. 조회된 유저를 Map으로 변환하여 빠르게 검색 가능하도록 함. (닉네임 - Key, UserEntity - Value)
        Map<String, UserEntity> userMap = users.stream()
                .collect(Collectors.toMap(UserEntity::getNickname, Function.identity()));

        //DB에 저장할 초대 요청 리스트
        List<AppointmentInvitation> invitationsToSave = new ArrayList<>();

        //클라이언한테 반환할 DTO 리스트
        List<AppointmentInviteResponseDTO> invitedUsers = new ArrayList<>();

        for (String username : request.getNicknames()) {
            UserEntity user = userMap.get(username);
            if (user == null) {
                throw new UserEntityHandler(ErrorStatus.MEMBER_NOT_FOUND);
            }

            //이미 초대된 사용자가 존재하다면 continue
            boolean isInvited = appointmentInviteRepository.existsByAppointmentIdAndUserEntity(request.getAppointmentId(), user);
            if (isInvited) {
                continue;
            }

            //3. 초대 요청을 위한 객체 생성 후 리스트에 추가 (Bulk insert)
            AppointmentInvitation invitation = AppointmentInvitation.builder()
                    .appointmentId(request.getAppointmentId())
                    .userEntity(user)
                    .status(InvitationStatus.PENDING)
                    .build();

            invitationsToSave.add(invitation);
            invitedUsers.add(converter.toResponseDTO(user, InvitationStatus.PENDING));
        }

        //4. Batch insert
        appointmentInviteRepository.saveAll(invitationsToSave);

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
