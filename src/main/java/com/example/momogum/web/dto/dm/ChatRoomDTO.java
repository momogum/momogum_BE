package com.example.momogum.web.dto.dm;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class ChatRoomDTO {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    // 채팅방 단일 조회 응답 DTO
    public static class ChatRoomResponse {
        private MessageDTO messageDTO;
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    // 채팅방 목록 조회 응답 DTO
    public static class ChatRoomListResponse {

        private ChatRoomSummaryList chatRoomSummaryList;
    }

    // 채팅방 이름 변경 요청 DTO
    @Getter
    public static class ChangeRoomNameRequest {

        private String newRoomName;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    // 채팅방 이름 변경 응답 DTO
    public static class ChangeRoomNameResponse {

        private Long roomId;
        private String newRoomName;
    }

    // 채팅방 검색 요청 DTO
    @Getter
    public static class SearchChatRoomRequest {
        private String keyword;
    }

    // 채팅방 검색 응답 DTO
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class SearchChatRoomResponse {
        private Long roomId;
    }

    // 채팅방 생성 요청 DTO
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class CreateChatRoomRequest {

        private List<Long> memberIds;
    }

    // 채팅방 생성 응답 DTO
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class CreateChatRoomResponse {
        private Long roomId;
    }

    // 채팅방 고정 요청 DTO
    @Getter
    public static class PinChatRoomRequest {
        private Long roomId;
    }

    // 채팅방 고정 응답 DTO
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class PinChatRoomResponse {
        private Long roomId;
    }


    // 채팅방 나가기 응답 DTO
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class LeaveChatRoomResponse {

        private Long leaveRoomId;
    }


    //-----------------------------------------------------------------------------//

    // 채팅방 목록 조회 때 간략한 형태의 채팅방 내용
    public static class ChatRoomSummary {

        private Long roomId;

        private String roomName;

        private LocalDate lastMessageTime;

        private String lastMessage;

        @Schema(description = "채팅방 이미지입니다.")
        private String roomImagePath;
    }

    // 간략한 채팅방 List 형태
    public static class ChatRoomSummaryList {

        private List<ChatRoomSummary> chatRoomSummaryList;
    }


}
