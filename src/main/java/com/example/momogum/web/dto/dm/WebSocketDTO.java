package com.example.momogum.web.dto.dm;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


public class WebSocketDTO {


    public static class WebSocketMessageDTO {

        @Schema(description = "소켓 통신에 사용될 타입입니다.")
        private String type;

        @Schema(description = "채팅방 id가 들어갑니다.")
        private Long roomId;

        @Schema(description = "")
        private Long senderId;

        @Schema(description = "")
        private String content;

        @Schema(description = "")
        private LocalDateTime sentAt;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class HandShakeResponse {

        private String webSocketUrl;
        private String message;
    }

}
