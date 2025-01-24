package com.example.momogum.domain.common.enums;

import lombok.Getter;

@Getter
public enum ReportReason {

    WRONG_INFO("잘못된 정보"),
    COMMERCIAL_ADV("상업적 광고"),
    PORNO("음란물"),
    VIOLET("폭력성");


    private final String reason;

    ReportReason(String reason) {
        this.reason = reason;
    }
}
