package com.example.momogum.service.userProfileService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.GeneralException;
import com.example.momogum.converter.ViewMealDiaryConverter;
import com.example.momogum.converter.followConverter.FollowConverter;
import com.example.momogum.converter.followConverter.TargetFollowConverter;
import com.example.momogum.converter.mealDiaryConverter.MealDiaryConverter;
import com.example.momogum.converter.userConverter.TargetProfileConverter;
import com.example.momogum.converter.userConverter.UserProfileConverter;
import com.example.momogum.domain.MealDiaryBookmark;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.repository.followRepo.FollowerRepository;
import com.example.momogum.repository.followRepo.FollowingRepository;
import com.example.momogum.repository.mealDiaryBookmarkRepo.MealDiaryBookmarkRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.web.dto.FollowDTO;
import com.example.momogum.web.dto.user.UserDTO;
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

  /**
   * 상대 유저의 프로필 정보 조회
   */
  @Override
  @Transactional(readOnly = true)
  public UserDTO.FullProfileDTO getTargetProfile(Long targetUserId, boolean isFollowing) {
    UserEntity targetUser = userEntityRepository.findById(targetUserId)
        .orElseThrow(() -> new GeneralException(ErrorStatus.USER_PROFILE_NOT_FOUND));

    List<ViewMealDiaryResponse> mealDiaries = getTargetMealDiaries(targetUserId);

    return TargetProfileConverter.toFullProfileDTO(targetUser, mealDiaries, isFollowing);
  }

  /**
   * 상대 유저의 밥일기 목록 조회
   */
  @Override
  @Transactional(readOnly = true)
  public List<ViewMealDiaryDTO.ViewMealDiaryResponse> getTargetMealDiaries(Long targetUserId) {
    UserEntity targetUser = userEntityRepository.findById(targetUserId)
        .orElseThrow(() -> new GeneralException(ErrorStatus.USER_PROFILE_NOT_FOUND));

    return mealDiaryRepository.findByUserEntity(targetUser)
        .stream()
        .map(ViewMealDiaryConverter::toViewMealDiaryResponse)
        .collect(Collectors.toList());
  }

  /**
   * 상대 유저가 저장한 밥일기 목록 조회
   */
  @Override
  @Transactional(readOnly = true)
  public List<ViewMealDiaryDTO.ViewMealDiaryResponse> getTargetBookmarkedMealDiaries(Long targetUserId) {
    UserEntity targetUser = userEntityRepository.findById(targetUserId)
        .orElseThrow(() -> new GeneralException(ErrorStatus.USER_PROFILE_NOT_FOUND));

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
  public List<FollowDTO.FollowingResponseDTO> getTargetFollowings(Long currentUserId, Long targetUserId) {
    UserEntity targetUser = userEntityRepository.findById(targetUserId)
        .orElseThrow(() -> new GeneralException(ErrorStatus.USER_PROFILE_NOT_FOUND));

    UserEntity currentUser = userEntityRepository.findById(currentUserId)
        .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

    return followingRepository.findByUserId(targetUserId).stream()
        .map(following -> TargetFollowConverter.toFollowingResponseDTO(
            following.getFollowing(),
            followService.isMutualFollow(currentUser, following.getFollowing())
        ))
        .collect(Collectors.toList());
  }


  /**
   * 상대 유저의 팔로워 목록 조회 (상대를 팔로우하는 사람들)
   */
  @Override
  @Transactional(readOnly = true)
  public List<FollowDTO.FollowerResponseDTO> getTargetFollowers(Long currentUserId, Long targetUserId) {
    UserEntity targetUser = userEntityRepository.findById(targetUserId)
        .orElseThrow(() -> new GeneralException(ErrorStatus.USER_PROFILE_NOT_FOUND));

    UserEntity currentUser = userEntityRepository.findById(currentUserId)
        .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

    return followerRepository.findByUserId(targetUserId).stream()
        .map(follower -> TargetFollowConverter.toFollowerResponseDTO(
            follower.getFollower(),
            followService.isMutualFollow(currentUser, follower.getFollower())
        ))
        .collect(Collectors.toList());
  }

  /**
   * 맞팔 여부 확인 메서드
   */
  public Boolean isMutualFollow(UserEntity currentUser, UserEntity targetUser) {
    return followerRepository.existsByUserAndFollower(targetUser, currentUser)
        && followerRepository.existsByUserAndFollower(currentUser, targetUser);
  }

}
