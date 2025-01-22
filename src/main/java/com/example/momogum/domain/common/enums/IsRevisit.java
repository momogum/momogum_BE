package com.example.momogum.domain.common.enums;

import lombok.Getter;

@Getter
public enum IsRevisit {

    NOT_GOOD("아니요"),
    SO_SO("그저 그래요"),
    GOOD("또 오고 싶어요");

    private final String description;

    IsRevisit(String description) {
        this.description = description;
    }
}
