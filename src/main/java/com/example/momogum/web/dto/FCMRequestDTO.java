package com.example.momogum.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FCMRequestDTO {
    private Long userId;
    private String title;
    private String body;
}
