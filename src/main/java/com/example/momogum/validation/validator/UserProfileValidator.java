package com.example.momogum.validation.validator;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.GeneralException;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProfileValidator {

  private final UserEntityRepository userEntityRepository;

  // 최소 5 ~ 최대 20자
  // 영어 소문자, 숫자, 특수문자까지 허용
  private static final String NICKNAME_PATTERN = "^[a-z0-9!@#$%^&*()_+\\-=]{5,20}$";
  // 1~12자의 한글 또는 영문만 허용
  private static final String NAME_PATTERN = "^[a-zA-Z가-힣]{1,12}$";
  private static final int ABOUT_MAX_LENGTH = 40;

  /**
   * 닉네임 패턴 검사 + 중복 검사
   */
  public void validateNickname(String nickname) {
    if (nickname == null || !Pattern.matches(NICKNAME_PATTERN, nickname)) {
      throw new GeneralException(ErrorStatus.INVALID_NICKNAME_FORMAT);
    }
    // 닉네임 중복 검사
    if (userEntityRepository.existsByNickname(nickname)) {
      throw new GeneralException(ErrorStatus.DUPLICATE_NICKNAME);
    }
  }

  /**
   * 유저이름(name) 패턴 검사 (12자 이내의 한글 또는 영문만 허용)
   */
  public void validateName(String name) {
    if (name == null || !Pattern.matches(NAME_PATTERN, name)) {
      throw new GeneralException(ErrorStatus.INVALID_NAME_FORMAT);
    }
  }

  /**
   * 유저 한줄 소개(about) 검증 (0~40자)
   * 0이면 빈 문자열("")을 반환.
   */
  public String validateAbout(String about) {
    if (about == null || about.trim().isEmpty()) {
      return ""; // null 또는 빈 값이면 빈 문자열 반환
    }
    if (about.length() > ABOUT_MAX_LENGTH) {
      throw new GeneralException(ErrorStatus.INVALID_ABOUT_LENGTH);
    }
    return about;
  }
}
