package com.example.momogum.fcm;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FcmMessage {
    private boolean    validateOnly;
    private Message    message;

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message {
        private Notification notification;
        private String      token;
        private Data        data;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Notification {
        private String  title;
        private String  body;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Data{
        private String    name;
        private String    description;
    }
}