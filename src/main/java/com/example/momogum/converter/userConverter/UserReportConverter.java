package com.example.momogum.converter.userConverter;

import com.example.momogum.domain.Report;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.web.dto.user.UserReportDTO;

public class UserReportConverter {

  public static Report toUserReport(UserEntity reporter, UserEntity reportedUser) {
    return Report.builder()
        .reporterUser(reporter)
        .reportedUser(reportedUser)
        .build();
  }

  public static UserReportDTO.UserReportResponseDTO toUserReportResponseDTO(UserEntity reportedUser) {
    return UserReportDTO.UserReportResponseDTO.builder()
        .reportedUserId(reportedUser.getId())
        .message("신고가 접수되었습니다.<br>"+"검토는 최대 24시간 소요됩니다.")
        .build();
  }

}
