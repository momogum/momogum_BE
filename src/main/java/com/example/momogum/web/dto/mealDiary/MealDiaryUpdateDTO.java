package com.example.momogum.web.dto.mealDiary;

import com.example.momogum.domain.common.enums.FoodCategory;
import com.example.momogum.domain.common.enums.IsRevisit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MealDiaryUpdateDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MealDiaryUpdateRequestDTO{

        @Schema(description = "회원의 식별자를 입력 받습니다 <br>," +
                "추후에 JWT Token으로 변경될 수 있습니다")
        Long memberId;

        @Schema(description = "업데이트 대상 밥일기 입니다")
        Long mealDiaryId;

        @Schema(description = "식사 카테고리 입니다 <br>," +
                "정해진 카테고리에서 선택할 수 있도록 구현하였습니다")
        FoodCategory foodCategory;

        // 문장으로 받으면 쉼표를 기준으로 파싱하기
        @Schema(description = "키워드 입니다 <br>," +
                "정해진 키워드에서 선택할 수 있도록 구현하였습니다")
        String keyword;

        @Schema(description = "식사한 위치 입니다")
        String location;

        @Schema(description = "식사한 후기 입니다")
        String description;

        @Schema(description = "재방문 의사 입니다 <br>," +
                "기획안에 적혀있는 다섯가지의 선택지 내에서 정보를 선택 받습니다")
        IsRevisit revisit;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MealDiaryUpdateResponseDTO{

        @Schema(description = "업데이트 된 밥일기 식별자 입니다")
        Long mealDiaryId;

    }
}
