package com.example.momogum.web.dto.dm;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class WebSocketMessageDTO {
    @Schema(description = "소켓 통신에 사용될 타입입니다.")
    private String type;

    @Schema(description = "")
    private Long roomId;

    @Schema(description = "")
    private Long senderId;

    @Schema(description = "")
    private String content;

    @Schema(description = "")
    private LocalDateTime sentAt;
}
