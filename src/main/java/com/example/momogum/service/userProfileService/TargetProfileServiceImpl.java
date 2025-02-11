package com.example.momogum.service.userProfileService;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.GeneralException;
import com.example.momogum.converter.ViewMealDiaryConverter;
import com.example.momogum.converter.mealDiaryConverter.MealDiaryConverter;
import com.example.momogum.converter.userConverter.TargetProfileConverter;
import com.example.momogum.converter.userConverter.UserProfileConverter;
import com.example.momogum.domain.MealDiaryBookmark;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.repository.mealDiaryBookmarkRepo.MealDiaryBookmarkRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
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
  private final FollowServiceImpl followService;

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


}
