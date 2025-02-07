package com.example.momogum.service.userProfileService;


import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.GeneralException;
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


  // 유저 닉네임, 실명, 프로필 이미지 조회

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

    // 닉네임 검증 및 중복 검사
    if (request.getNickname() != null && !request.getNickname().equals(user.getNickname())) {
      userProfileValidator.validateNickname(request.getNickname());
      user.setNickname(request.getNickname());
    }
    // 유저 이름 검증
    if (request.getName() != null && !request.getName().equals(user.getName())) {
      userProfileValidator.validateName(request.getName());
      user.setName(request.getName());
    }
    // 한줄 소개 검증 (빈 문자열 반환 가능)
    user.setAbout(userProfileValidator.validateAbout(request.getAbout()));


    return UserDTO.UserEditDTO.builder()
        .nickname(user.getNickname())
        .name(user.getName())
        .about(user.getAbout() != null ? user.getAbout() : "")
        .build();
  }

  // 상대방 유저 프로필 조회

  @Override
  @Transactional
  public UserDTO.FullProfileDTO getFullProfile(Long userId) {
    UserEntity user = userEntityRepository.findById(userId)
        .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

    return UserProfileConverter.toFullProfileDTO(user);
  }

  // 내가 작성한 밥일기 조회
  @Override
  @Transactional(readOnly = true)
  public List<ViewMealDiaryResponse> getUserMealDiaries(Long userId) {
    UserEntity user = userEntityRepository.findById(userId)
        .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

    List<MealDiary> mealDiaries = mealDiaryRepository.findByUserEntity(user);

    return mealDiaries.stream()
        .map(mealDiary -> ViewMealDiaryDTO.ViewMealDiaryResponse.builder()
            .mealDiaryId(mealDiary.getId())
//            .foodImageURLs(mealDiary.getMealDiaryImages().stream()
//                .map(MealDiaryImage::getImageLink)
//                .toList()) 밥일기 이미지 구현되면 수정하겠습니다.
            .userImageURL(user.getProfileImage() != null
                ? user.getProfileImage().getImageLink()
                : "default-profile.jpg")
            .foodCategory(mealDiary.getFoodCategory())
            .keyWord(mealDiary.getMealDiaryKeywords().stream()
                .map(mealDiaryKeyword -> mealDiaryKeyword.getKeyword().getKeyword())
                .toList())
            .isRevisit(mealDiary.getIsRevisit())
            .build()
        ).toList();
  }

  // 북마크한 밥일기 조회
  @Override
  @Transactional(readOnly = true)
  public List<ViewMealDiaryDTO.ViewMealDiaryResponse> getBookmarkedMealDiaries(Long userId) {
    UserEntity user = userEntityRepository.findById(userId)
        .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

    List<MealDiaryBookmark> bookmarks = mealDiaryBookmarkRepository.findByUserEntity(user);

    return bookmarks.stream()
        .map(bookmark -> ViewMealDiaryDTO.ViewMealDiaryResponse.builder()
            .mealDiaryId(bookmark.getMealDiary().getId())
//            .foodImageURLs(bookmark.getMealDiary().getMealDiaryImages().stream()
//                .map(MealDiaryImage::getImageLink)
//                .toList())
            .userImageURL(bookmark.getMealDiary().getUserEntity().getProfileImage() != null
                ? bookmark.getMealDiary().getUserEntity().getProfileImage().getImageLink()
                : "default-profile.jpg")
            .foodCategory(bookmark.getMealDiary().getFoodCategory())
            .keyWord(bookmark.getMealDiary().getMealDiaryKeywords().stream()
                .map(mealDiaryKeyword -> mealDiaryKeyword.getKeyword().getKeyword())
                .toList())
            .isRevisit(bookmark.getMealDiary().getIsRevisit())
            .build()
        ).toList();
  }
}