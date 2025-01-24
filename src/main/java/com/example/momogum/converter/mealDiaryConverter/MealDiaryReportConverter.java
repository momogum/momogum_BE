package com.example.momogum.converter.mealDiaryConverter;

import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryReport;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.common.enums.ReportReason;
import com.example.momogum.web.dto.mealDiary.MealDiaryReportDTO;

public class MealDiaryReportConverter {

    public static MealDiaryReport toMealDiaryReport(UserEntity userEntity, MealDiary mealDiary, ReportReason reportReason) {

        return MealDiaryReport.builder()
                .userEntity(userEntity)
                .mealDiary(mealDiary)
                .reportReason(reportReason)
                .build();
    }

    public static MealDiaryReportDTO.MealDiaryReportResponseDTO mealDiaryReportResponseDTO(MealDiary mealDiary){
        return MealDiaryReportDTO.MealDiaryReportResponseDTO.builder()
                .mealDiaryId(mealDiary.getId())
                .build();
    }
}
