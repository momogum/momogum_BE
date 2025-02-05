package com.example.momogum.web.dto.search;

import com.example.momogum.domain.common.enums.FoodCategory;
import com.example.momogum.domain.common.enums.IsRevisit;
import com.example.momogum.web.dto.viewMealDiary.ViewMealDiaryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SearchDTO {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class AccountSearchResponseDTO {

        @Schema(description = "회원 Id")
        private Long userId;

        @Schema(description = "회원 계정 아이디")
        private String userName;

        @Schema(description = "회원 이름")
        private String userNickName;

        @Schema(description = "회원 프로필 사진 URL")
        private String userImageURL;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class PostSearchResponseDTO {

        @Schema(description = "밥일기 Id")
        private Long mealDiaryId;

        @Schema(description = "음식 사진 URL 입니다.")
        private String foodImageURL;

        @Schema(description = "회원 프로필 사진 URL")
        private String userImageURL;

        @Schema(description = "식사 메뉴")
        private String foodName;

        @Schema(description = "재방문 의사 표시 (REVISIT 이 재방문)")
        private IsRevisit isRevisit;
    }


}
