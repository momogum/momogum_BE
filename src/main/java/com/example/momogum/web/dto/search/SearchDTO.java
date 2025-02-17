package com.example.momogum.web.dto.search;

import com.example.momogum.domain.common.enums.IsRevisit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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

        @Schema(description = "검색 결과 중 사용자가 팔로우 한 사람 중 검색결과 대상을 팔로우한 사람 이름")
        private List<String> searchFollowName;

        @Schema(description = "검색 결과 중 사용자가 팔로우한 사람아 검색결과 대상을 팔로우 한 수")
        private Integer searchFollowCount;

        @Schema(description = "해당 사람 스토리 여부")
        private Boolean hasStory;

        @Schema(description = "스토리 확인 여부")
        private Boolean hasViewedStory;

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

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class FollowerSearchResponseDTO {
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
    public static class FollowingSearchResponseDTO {
        @Schema(description = "회원 Id")
        private Long userId;

        @Schema(description = "회원 계정 아이디")
        private String userName;

        @Schema(description = "회원 이름")
        private String userNickName;

        @Schema(description = "회원 프로필 사진 URL")
        private String userImageURL;
    }

}
