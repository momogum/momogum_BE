package com.example.momogum.web.dto.viewMealDiary;

import com.example.momogum.domain.common.enums.FoodCategory;
import com.example.momogum.domain.common.enums.IsRevisit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class ViewMealDiaryDTO {


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class ViewMealDiaryResponse {

        @Schema(description = "")
        private Long id;

        @Schema(description = "")
        private FoodCategory foodCategory;

        @Schema(description = "")
        private String keyWord;

        @Schema(description = "")
        private IsRevisit isRevisit;
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class ViewMealDiaryResponseListDTO {

        private List<ViewMealDiaryResponse> viewMealDiaryResponseList;
    }

}
