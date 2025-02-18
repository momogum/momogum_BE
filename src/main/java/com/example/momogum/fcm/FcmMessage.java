package com.example.momogum.fcm;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor  // 이 부분 추가
@Getter
public class FcmMessage {
    private boolean validateOnly;
    private Message message;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor  // 이 부분 추가
    @Getter
    public static class Message {
        private Notification notification;
        private String token;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor  // 이 부분 추가
    @Getter
    public static class Notification {
        private String title;
        private String body;
        private String image;
    }
}