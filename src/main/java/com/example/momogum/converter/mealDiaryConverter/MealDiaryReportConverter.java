package com.example.momogum.converter.mealDiaryConverter;

import com.example.momogum.domain.MealDiary;
import com.example.momogum.domain.MealDiaryReport;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.domain.common.enums.ReportReason;
import com.example.momogum.web.dto.mealDiary.MealDiaryReportDTO;

import java.util.List;

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

    public static List<MealDiaryReportDTO.MealDiaryReportResponseDTO> toMealDiaryReportResponseDTOList(List<MealDiaryReport> allReport){

        return allReport.stream()
                .filter(report -> report.getMealDiary() != null)
                .map(report -> MealDiaryReportDTO.MealDiaryReportResponseDTO.builder()
                        .mealDiaryId(report.getMealDiary().getId())
                        .build())
                .toList();

    }
}
