package com.example.momogum.service.userProfileService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.GeneralException;
import com.example.momogum.apiPayLoad.exception.handler.UserEntityHandler;
import com.example.momogum.converter.ViewMealDiaryConverter;
import com.example.momogum.converter.followConverter.TargetFollowConverter;
import com.example.momogum.converter.userConverter.TargetProfileConverter;
import com.example.momogum.converter.userConverter.UserReportConverter;
import com.example.momogum.domain.MealDiaryBookmark;
import com.example.momogum.domain.Report;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.repository.followRepo.FollowerRepository;
import com.example.momogum.repository.followRepo.FollowingRepository;
import com.example.momogum.repository.mealDiaryBookmarkRepo.MealDiaryBookmarkRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryRepository;
import com.example.momogum.repository.reportRepo.ReportRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.FollowDTO;
import com.example.momogum.web.dto.user.UserDTO;
import com.example.momogum.web.dto.user.UserReportDTO;
import com.example.momogum.web.dto.viewMealDiary.ViewMealDiaryDTO;
import com.example.momogum.web.dto.viewMealDiary.ViewMealDiaryDTO.ViewMealDiaryResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TargetProfileServiceImpl implements TargetProfileService {

  private final UserEntityRepository userEntityRepository;
  private final MealDiaryRepository mealDiaryRepository;
  private final MealDiaryBookmarkRepository mealDiaryBookmarkRepository;
  private final FollowerRepository followerRepository;
  private final FollowingRepository followingRepository;
  private final FollowService followService;
  private final ReportRepository reportRepository;

  /**
   * 상대 유저의 프로필 정보 조회
   */
  @Override
  @Transactional(readOnly = true)
  public UserDTO.FullProfileDTO getTargetProfile(Long currentUserId, Long targetUserId) {
    UserEntity targetUser = getTargetUser(targetUserId);

    return TargetProfileConverter.toFullProfileDTO(targetUser, getTargetMealDiaries(currentUserId, targetUserId));
  }

  /**
   * 상대 유저의 밥일기 목록 조회
   */
  @Override
  @Transactional(readOnly = true)
  public List<ViewMealDiaryResponse> getTargetMealDiaries(Long currentUserId, Long targetUserId) {
    // 대상 사용자와 현재 사용자 조회 (필요 시 현재 사용자 정보가 사용되므로)
    UserEntity targetUser = getTargetUser(targetUserId);
    getCurrentUser(currentUserId);

    return mealDiaryRepository.findByUserEntity(targetUser).stream()
        .map(ViewMealDiaryConverter::toViewMealDiaryResponse)
        .collect(Collectors.toList());
  }

  /**
   * 상대 유저가 저장한 밥일기 목록 조회
   */
  @Override
  @Transactional(readOnly = true)
  public List<ViewMealDiaryResponse> getTargetBookmarkedMealDiaries(Long currentUserId, Long targetUserId) {
    UserEntity targetUser = getTargetUser(targetUserId);
    getCurrentUser(currentUserId);

    List<MealDiaryBookmark> bookmarks = mealDiaryBookmarkRepository.findByUserEntity(targetUser);

    return bookmarks.stream()
        .map(ViewMealDiaryConverter::toViewMealDiaryResponse)
        .collect(Collectors.toList());
  }

  /**
   * 상대 유저의 팔로잉 목록 조회 (상대가 팔로우하는 사람들)
   */
  @Override
  @Transactional(readOnly = true)
  public List<FollowDTO.FollowingResponseDTO> getTargetFollowings(Long targetUserId) {
    UserEntity targetUser = getTargetUser(targetUserId);
    return followingRepository.findByUserId(targetUserId).stream()
        .map(following -> TargetFollowConverter.toFollowingResponseDTO(following.getFollowing()))
        .collect(Collectors.toList());
  }

  /**
   * 상대 유저의 팔로워 목록 조회 (상대를 팔로우하는 사람들)
   */
  @Override
  @Transactional(readOnly = true)
  public List<FollowDTO.FollowerResponseDTO> getTargetFollowers(Long targetUserId) {
    UserEntity targetUser = getTargetUser(targetUserId);
    return followerRepository.findByUserId(targetUserId).stream()
        .map(follower -> TargetFollowConverter.toFollowerResponseDTO(follower.getFollower()))
        .collect(Collectors.toList());
  }

  /**
   * 상대 유저 신고하기 메서드
   */
  @Override
  @Transactional
  public UserReportDTO.UserReportResponseDTO report(Long reporterId, UserReportDTO.UserReportRequestDTO request) {
    UserEntity reporter = getCurrentUser(reporterId); // 신고자 조회
    UserEntity reportedUser = getCurrentUser(request.getReportedUserId()); // 신고 대상 조회 (필요에 따라 대상용 헬퍼 메서드를 사용해도 됩니다)

    Report newReport = UserReportConverter.toUserReport(reporter, reportedUser);

    reportRepository.save(newReport);

    return UserReportConverter.toUserReportResponseDTO(reportedUser);
  }

  // 대상 사용자 조회 (프로필 조회 시 대상 사용자에 대해 별도의 에러 메시지 사용)
  private UserEntity getTargetUser(Long userId) {
    return userEntityRepository.findById(userId)
        .orElseThrow(() -> new GeneralException(ErrorStatus.USER_PROFILE_NOT_FOUND));
  }

  // 현재 사용자 조회 (로그인 등 현재 사용자에 대한 에러 메시지)
  private UserEntity getCurrentUser(Long userId) {
    return userEntityRepository.findById(userId)
        .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
  }

}
