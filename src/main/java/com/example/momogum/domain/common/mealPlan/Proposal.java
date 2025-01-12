package com.example.momogum.domain.common.mealPlan;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public enum Proposal {

    @Schema(description = "커피 한 잔 어때요?")
    COFFEE("커피 한 잔 어때요?"),

    @Schema(description = "밥 한 끼 어때요?")
    MEAL("밥 한 끼 어때요?");

    private final String description;

    Proposal(String description) {
        this.description = description;
    }

}
