package com.example.momogum.web.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

// 밥일기 DTO
public class MealDiaryDTO {

  @Getter
  @Builder
  @AllArgsConstructor
  public static class Response {
    private final int year;                // 연도
    private final int month;               // 월
    private final List<ImageEntry> images; // 이미지 목록

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ImageEntry {
      private final String date;         // 날짜
      private final String imagePath;    // 이미지 경로
    }
  }

  @Getter
  @Builder
  @AllArgsConstructor
  public static class Detail {
    private final String date;            // 날짜
    private final String imagePath;       // 이미지 경로
  }
}
