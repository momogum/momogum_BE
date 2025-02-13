package com.example.momogum.service.userProfileService;


import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.GeneralException;
import com.example.momogum.converter.ViewMealDiaryConverter;
import com.example.momogum.converter.userConverter.UserProfileConverter;
import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryBookmark;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.repository.mealDiaryBookmarkRepo.MealDiaryBookmarkRepository;
import com.example.momogum.repository.mealDiaryRepo.MealDiaryRepository;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.validation.validator.UserProfileValidator;
import com.example.momogum.web.dto.user.UserDTO;
import com.example.momogum.web.dto.viewMealDiary.ViewMealDiaryDTO;
import com.example.momogum.web.dto.viewMealDiary.ViewMealDiaryDTO.ViewMealDiaryResponse;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

  private final UserEntityRepository userEntityRepository;
  private final UserProfileValidator userProfileValidator;
  private final MealDiaryRepository mealDiaryRepository;
  private final MealDiaryBookmarkRepository mealDiaryBookmarkRepository;

  private void updateFieldIfValid(
      String newValue,
      String currentValue,
      Consumer<String> validator,
      Consumer<String> setter) {

    if (newValue != null && !newValue.equals(currentValue)) {
      validator.accept(newValue);
      setter.accept(newValue);
    }
  }

  // 한줄 소개 검증 (변환된 값 반환)
  private void updateFieldIfValid(
      String newValue,
      Function<String, String> validator,  // 검증 후 변환된 값 반환
      Consumer<String> setter) {  // setter 실행

    if (newValue != null) {
      String validatedValue = validator.apply(newValue);
      setter.accept(validatedValue);
    }
  }


  // 유저 닉네임, 실명, 프로필 이미지 조회 FIXME 이미지 할당
  @Override
  @Transactional(readOnly = true)
  public UserDTO.UserResponseDTO getUserProfile(Long userId) {
    UserEntity user = userEntityRepository.findById(userId)
        .orElseThrow(() -> new GeneralException(ErrorStatus._BAD_REQUEST));

    return UserDTO.UserResponseDTO.builder()
        .id(user.getId())
        .nickname(user.getNickname())
        .name(user.getName())
        .profileImage(user.getProfileImage() != null
            ? user.getProfileImage().getImageLink()
            : "default-profile.jpg")
        .about(user.getAbout())
        .build();
  }

  // 유저 프로필 업데이트

  @Override
  @Transactional
  public UserDTO.UserEditDTO updateUserProfile(Long userId, UserDTO.UserEditDTO request) {
    UserEntity user = userEntityRepository.findById(userId)
        .orElseThrow(() -> new GeneralException(ErrorStatus._BAD_REQUEST));

    updateFieldIfValid(request.getNickname(), user.getNickname(),
        userProfileValidator::validateNickname, user::setNickname);

    updateFieldIfValid(request.getName(), user.getName(),
        userProfileValidator::validateName, user::setName);

    updateFieldIfValid(request.getAbout(),
        userProfileValidator::validateAbout, user::setAbout);

    return UserDTO.UserEditDTO.builder()
        .nickname(user.getNickname())
        .name(user.getName())
        .about(user.getAbout() != null ? user.getAbout() : "")
        .build();
  }


  // 내가 작성한 밥일기 조회
  @Override
  @Transactional(readOnly = true)
  public List<ViewMealDiaryResponse> getUserMealDiaries(Long userId) {
    UserEntity user = userEntityRepository.findById(userId)
        .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

    List<MealDiary> mealDiaries = mealDiaryRepository.findByUserEntity(user);

    return mealDiaries.stream()
        .map(ViewMealDiaryConverter::toViewMealDiaryResponse)
        .collect(Collectors.toList());
  }

  // 북마크한 밥일기 조회
  @Override
  @Transactional(readOnly = true)
  public List<ViewMealDiaryResponse> getBookmarkedMealDiaries(Long userId) {
    UserEntity user = userEntityRepository.findById(userId)
        .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

    List<MealDiaryBookmark> bookmarks = mealDiaryBookmarkRepository.findByUserEntity(user);

    return bookmarks.stream()
        .map(ViewMealDiaryConverter::toViewMealDiaryResponse)
        .collect(Collectors.toList());
  }
}
