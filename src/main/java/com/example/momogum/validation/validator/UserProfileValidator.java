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

  /**
   * 닉네임 유효성 검사 + 중복 검사
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
   * 이름(name) 유효성 검사 (12자 이내의 한글 또는 영문만 허용)
   */
  public void validateName(String name) {
    if (name == null || !Pattern.matches(NAME_PATTERN, name)) {
      throw new GeneralException(ErrorStatus.INVALID_NAME_FORMAT);
    }
  }
}
