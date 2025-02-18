package com.example.momogum.fcm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDTO {
    private Long userId;
    private String title;
    private String body;
}
