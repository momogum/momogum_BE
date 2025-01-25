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

        @Schema(description = "밥일기 Id")
        private Long mealDiaryId;

        @Schema(description = "음식 사진 URL 입니다.")
        private List<String> foodImageURLs;

        @Schema(description = "회원 프로필 사진 URL")
        private String userImageURL;

        @Schema(description = "음식 카테고리 (한식(KOREA),중식(CHINA),일식(JAPAN),아시안(ASIAN),패스트푸드(FASTFOOD),카페(CAFE)")
        private FoodCategory foodCategory;

        @Schema(description = "음식 이름")
        private List<String> keyWord;

        @Schema(description = "재방문 의사 표시 (REVISIT 이 재방문)")
        private IsRevisit isRevisit;
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder(toBuilder = true)
    public static class ViewMealDiaryResponseListDTO {

        private List<ViewMealDiaryResponse> viewMealDiaryResponseList;
    }

}
