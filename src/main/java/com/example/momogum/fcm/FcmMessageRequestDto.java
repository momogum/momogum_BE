package com.example.momogum.fcm;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FcmMessageRequestDto {

    @Schema(description = "유저ID")
    private Long userId;

    @Schema(description = "메시지 제목")
    private String title;

    @Schema(description = "메시지 내용")
    private String body;
}