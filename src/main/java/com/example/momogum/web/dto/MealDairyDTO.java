package com.example.momogum.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class MealDairyDTO {

    @Getter
    public static class CreateStoryRequestDTO {

        // 시큐리티 구현되는대로 수정하기 FIXME
        @Schema(description = "회원의 식별자를 입력 받습니다 <br>," +
                "추후에 JWT Token으로 변경될 수 있습니다")
        Long memberId;

        // 카테고리 정해지는대로 String -> FoodCategory(Enum.class)로 수정하기 FIXME
        @Schema(description = "식사 카테고리 입니다 <br>," +
                "정해진 카테고리에서 선택할 수 있도록 구현하였습니다")
        String foodCategory;

        // 키워드 정해지는대로 String -> Keyword(Enum.class)로 수정하기 FIXME
        @Schema(description = "키워드 입니다 <br>," +
                "정해진 키워드에서 선택할 수 있도록 구현하였습니다")
        String keyword;

        @Schema(description = "메뉴 이름 입니다")
        String menu;

        @Schema(description = "식사한 위치 입니다")
        String location;

        @Schema(description = "식사한 후기 입니다")
        String review;

        // String -> Revisit(Enum.class)로 수정하기 FIXME
        @Schema(description = "재방문 의사 입니다 <br>," +
                "기획안에 적혀있는 다섯가지의 선택지 내에서 정보를 선택 받습니다")
        String revisit;

        @Max(5)
        @Min(0)
        @Schema(description = "평점 입니다 <br>," +
                "0.1점 간격으로, 최대 5점까지 입력 받습니다")
        Integer score;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateStoryResponseDTO {

        @Schema(description = "스토리 식별ID 입니다")
        Long storyId;

    }






    @Getter
    public static class GetStoryRequestDTO {

        @Schema(description = "스토리 식별ID 입니다")
        Long storyId;

    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetStoryResponseDTO {

        @Max(5)
        @Min(0)
        @Schema(description = "평점 입니다 <br>," +
                "0.1점 간격으로, 최대 5점까지 입력 받습니다")
        Integer score;

        // 카테고리 정해지는대로 String -> FoodCategory(Enum.class)로 수정하기 FIXME
        @Schema(description = "식사 카테고리 입니다 <br>," +
                "정해진 카테고리에서 선택할 수 있도록 구현하였습니다")
        String foodCategory;

        // 키워드 정해지는대로 String -> Keyword(Enum.class)로 수정하기 FIXME
        @Schema(description = "키워드 입니다 <br>," +
                "정해진 키워드에서 선택할 수 있도록 구현하였습니다")
        String keyword;

        @Schema(description = "식사한 위치 입니다")
        String location;

        @Schema(description = "식사한 후기 입니다")
        String review;

        @Schema(description = "스토리 이미지 입니다 <br>," +
                "추후 구현 방식에 따라 응답이 달라질 수 있습니다")
        String imagePath;

    }




    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetStoryFollowResponseDTO {

        @Schema(description = "스토리를 올린 회원의 프로필 이미지 입니다 <br>," +
                "추후 구현 방식에 따라 응답이 달라질 수 있습니다")
        String memberImagePath;

        @Schema(description = "스토리를 올린 회원의 Nickname 입니다")
        String nickname;

        @Schema(description = "스토리가 조회 된 적이 있는지를 표시하는 필드입니다")
        Boolean isRead;

    }



    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetStoryMemberStoryResponseDTO {

        @Schema(description = "스토리 이미지 입니다 <br>," +
                "추후 구현 방식에 따라 응답이 달라질 수 있습니다")
        List<String> imagePaths;

    }
}
