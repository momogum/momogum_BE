package com.example.momogum.domain.common.enums;

import lombok.Getter;

@Getter
public enum FoodCategory {


    KOREAN("한식"),
    CHINESE("중식"),
    JAPANESE("일식"),
    WESTERN("양식"),
    ASIAN("아시안 푸드"),
    FAST_FOOD("패스트 푸드"),
    CAFE("카페"),
    ETC("기타");

    private final String description;

    FoodCategory(String description) {
        this.description = description;
    }
}
