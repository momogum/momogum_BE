package com.example.momogum.web.dto.user;

import com.example.momogum.domain.common.enums.ReportReason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserReportDTO {

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class UserReportRequestDTO{
      private Long reportedUserId;

      ReportReason reportReason;
}

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class UserReportResponseDTO{
    private Long reportedUserId;
    private String message;
  }

}
