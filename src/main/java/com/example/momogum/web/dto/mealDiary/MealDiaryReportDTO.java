package com.example.momogum.web.dto.mealDiary;

import com.example.momogum.domain.common.enums.ReportReason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MealDiaryReportDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MealDiaryReportRequestDTO{

//        Long userID;

        Long mealDiaryId;

        ReportReason reportReason;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MealDiaryReportResponseDTO{
        Long mealDiaryId;
    }
}
