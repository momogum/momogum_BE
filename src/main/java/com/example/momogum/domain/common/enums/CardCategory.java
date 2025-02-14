package com.example.momogum.domain.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CardCategory {
    BASIC("basic"),
    FUN("fun"),
    EVENT("event");

    private final String category;

    public String getCategory() {
        return category;
    }
}
