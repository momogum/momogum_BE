package com.example.momogum.web.dto.mealPlan;


import com.example.momogum.domain.common.enums.Proposal;
import com.example.momogum.web.dto.user.UserDTO.UserResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class MealPlanDTO {


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateMealPlanResponseDTO {

        @Schema(description = "약속잡기 식별 ID 입니다.")
        Long mealPlanId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MealPlanResponseDTO {

        @Schema(description = "약속잡기 식별 ID 입니다.")
        Long id;

        @Schema(description = "약속 제안입니다. ex) 커피 한 잔 할까요?")
        Proposal proposal;

        @Schema(description = "약속 제목입니다.")
        String title;


        @Schema(description = "식사 메뉴입니다.")
        String menu;

        @Schema(description = "약속 날짜입니다.")
        LocalDate date;

        @Schema(description = "약속 위치입니다.")
        String location;

        @Schema(description = "추가 메모입니다.")
        String notes;

        @Schema(description = "약속 생성 날짜입니다.")
        String createdAt;

        @Schema(description = "참여자 목록입니다.")
        List<UserResponseDTO> users;

    }

}
