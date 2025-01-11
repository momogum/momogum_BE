package com.example.momogum.web.controller;


import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.web.dto.dm.ChatRoomDTO;
import com.example.momogum.web.dto.dm.WebSocketDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/direct")
@Tag(name = "DM API")
public class DmController {

    /* 소켓 통신이라는 변수가 있어서 전반적으로 명세를 디테일 하게 잡아두지 않았습니다. 작업 시작되는 대로
        현재 뼈대를 토대로 디테일 하게 진행하겠습니다.
     */

    /*
    * dm 화면을 켰을 때 카카오톡이나 인스타 DM 처럼 각 채팅방 이름 마지막 채팅 내용, 시간, 읽었는지 여부 등
    보내줄 API입니다.
    */
    @GetMapping("/rooms")
    @Operation(summary = "채팅방 전체 조회 API",
            description = "dm 창 들어갔을 때 모든 채팅방이 보일 수 있도록 전체 조회하는 API입니다.<br> " +
                    "현재 구상중인 사항으론 각 채팅방에서 가장 마지막에 보낸 채팅 + 채팅방 이름 보이도록 생각중입니다.")
    public ApiResponse<ChatRoomDTO.ChatRoomListResponse> getChatRooms(
            @RequestParam Integer page,
            @RequestParam Integer size
    ) {
        return ApiResponse.onSuccess(ChatRoomDTO.ChatRoomListResponse.builder().build());
    }
    /*
    * 채팅방을 만들 때 사용 될 API입니다.
    * 초대할 사람들의 id를 받아서 진행될 예정입니다.
    * 이 과정 이후 바로 핸드셰이크가 진행되어 실시간 채팅이 진행될 듯 합니다.
    * */

    @PostMapping("/rooms")
    @Operation(summary = "채팅방 만들기 API",
            description = "처음 채팅 시작하기 눌러서 상대를 초대하여 진행되는 채팅방 만들기 API입니다." +
                    "<br>이때 채팅방으로 입장되면 밑에 작성되어 있는 핸드셰이크가 진행되어야 할 듯 합니다.")
    public ApiResponse<ChatRoomDTO.CreateChatRoomResponse> createChatRoom(
            @RequestBody ChatRoomDTO.CreateChatRoomRequest request
    ) {
        return ApiResponse.onSuccess(ChatRoomDTO.CreateChatRoomResponse.builder().build());
    }

    /*
        채팅방 단일 조회입니다. 이때 해당 채팅방에서 진행되었던 메세지를 dto 형식으로 리스트화하여 반환할 예정입니다.
        마찬가지로 채팅방에 들어가는 경우이기에 바로 핸드셰이크가 진행되면 될듯합니다.
    */
    @GetMapping("/rooms/{roomId}")
    @Operation(summary = "채팅방 단일 조회",
            description = "채팅방에 들어갔을 때 나와야하는 정보들을 담을 예정입니다. <br>" +
                    "양쪽에서 보낸 메세지들을 메세지DTO를 list화 하여 보내 드릴 예정입니다.")
    public ApiResponse<ChatRoomDTO.ChatRoomResponse> getChatRoom(
            @PathVariable Long roomId
    ) {
        return ApiResponse.onSuccess(ChatRoomDTO.ChatRoomResponse.builder().build());
    }


    /*
        채팅방 검색 기능을 담당할 예정입니다. keyword를 받아 해당 keyword를 통해 채팅방을 검색할 예정입니다.
        키워드는 채팅방 이름 + 사용자 이름에 한정되게 사용될 예정입니다.
    */
    @GetMapping("/rooms/search")
    @Operation(summary = "채팅방 검색 API",
            description = "채팅방 검색 기능을 담당할 API입니다.<br>" +
                    "채팅방을 검색했을 때 keyword라는 string 표현으로 request가 들어오면 해당 " +
                    "keyword를 통해서 검색할 예정입니다.")
    public ApiResponse<ChatRoomDTO.SearchChatRoomResponse> searchChatRooms(
            @RequestParam String keyword
    ) {
        return ApiResponse.onSuccess(ChatRoomDTO.SearchChatRoomResponse.builder().build());
    }

    /*
        채팅방 고정기능을 진행할 API입니다. 채팅방 고정에 있어서는 프론트분과 상의를 조금 거쳐야 할 듯합니다.
        만약 채팅방 고정을 할 경우 고정한 시간을 토대로 순서를 정해야하고, 최대 몇개까지 고정할지에 대해서도 고려를 해봐야 할 듯합니다.
    */
    @GetMapping("/rooms/{roomId}/pin")
    @Operation(summary = "채팅방 고정 API",
            description = "현재 고민중인 것은 고정된 기록을 db에서 다루어야할지 혹은 프론트 엔드에서 처리가 가능한지 입니다.<br>" +
                    " 논의 후 진행하겠습니다.")
    public ApiResponse<ChatRoomDTO.PinChatRoomResponse> pinChatRoom(
            @RequestBody ChatRoomDTO.PinChatRoomRequest request,
            @PathVariable Long roomId
    ) {
        return ApiResponse.onSuccess(ChatRoomDTO.PinChatRoomResponse.builder().build());
    }


    /*
        채팅방 나가기 API입니다. 1:1 채팅의 경우 양쪽 모두 채팅방에 나가기를 했을 경우에는 채팅방을 삭제를 하여 메세지를 다 날리는 방향으로 고려중입니다.
        메세지 기록을 관리하는 정도에 따라 해당 기능이 더 명확해질 예정입니다.
    */
    @PatchMapping("/rooms/{roomId}")
    @Operation(summary = "채팅방 나가기 API",
            description = "채팅방을 나갈 때 사용되는 API입니다. 양방향에서 진행되는 채팅이기에 한명이 나가도 다른 한명은 존재 해야해서 <br>" +
                    "boolean형태로 active 상태를 둘거고 해당 상태를 바꾸는 방향으로 설계했습니다.")
    public ApiResponse<ChatRoomDTO.LeaveChatRoomResponse> leaveChatRoom(
            @PathVariable Long roomId
    ) {
        return ApiResponse.onSuccess(ChatRoomDTO.LeaveChatRoomResponse.builder().build());
    }

    /*
        채팅방 이름 변경 API입니다. 해당 기능은 1:1 채팅이 아닌 단체 채팅방에서만 사용될 기능입니다. 1:1 채팅의 경우에는 상대방의 이름이
        채팅방의 이름으로 사용되고 단체 채팅의 경우에는 일관적으로 채팅방의 이름을 변경할 예정입니다.
    */
    @PatchMapping("rooms/{roomId}/name")
    @Operation(summary = "채팅방 이름 변경 API",
            description = "채팅방 이름을 변경하는 API입니다. 1:1 채팅이 아닌 여러명과의 채팅방에서만 적용 가능하게 할 생각입니다.")
    public ApiResponse<ChatRoomDTO.ChangeRoomNameResponse> changeRoomName(
            @RequestBody ChatRoomDTO.ChangeRoomNameRequest request,
            @PathVariable Long roomId
    ) {
        return ApiResponse.onSuccess(ChatRoomDTO.ChangeRoomNameResponse.builder().build());
    }

    //------------------------------------------------------------------------------------------------------------//

    //WS 핸드셰이크를 위한 API

    /*
        실시간 채팅을 진행하기 위한 첫 단추인 핸드셰이크 API 입니다. 해당 API 호출 이후에는 ws/api로 넘어가 채팅이 진행될 예정입니다.
    */
    @GetMapping("/rooms/{roomId}/handshake")
    @Operation(summary = "채팅 시작 API입니다.",
            description = "채팅방에 들어 갔을 때 해당 API가 사용 되어야 소켓 통신을 통한 실시간 채팅으로 전환됩니다.(핸드셰이크)")
    public ApiResponse<WebSocketDTO.HandShakeResponse> handShake(@PathVariable String roomId
    ) {
        return ApiResponse.onSuccess(WebSocketDTO.HandShakeResponse.builder().build());
    }

    /*
        ws api를 임시로 예시를 들어 두었습니다. 정확한 주소가 확정이 되면 수정하겠습니다.
    */
    @GetMapping("ws/chat")
    @Operation(summary = "웹소켓 API입니다.",
            description = "확정된 사안이 아닙니다. 웹소켓의 경우에는 따로 API가 있어 실행되기에 참고용으로 확인해주시면 감사하겠습니다. <br>" +
                    "빠른 시일 내에 개발 완료되면 직접 연락드리겠습니다.")
    public ApiResponse<WebSocketDTO.WebSocketMessageDTO> wsapiexampe() {
        WebSocketDTO.WebSocketMessageDTO example = new WebSocketDTO.WebSocketMessageDTO();
        return ApiResponse.onSuccess(example);
    }
}
