package com.example.momogum.web.controller;


import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.web.dto.TestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/direct")
@Tag(name = "DM API")
public class DmController {


    @GetMapping("/rooms")
    @Operation(summary = "채팅방 전체 조회 API",
            description = "dm 창 들어갔을 때 모든 채팅방이 보일 수 있도록 전체 조회하는 API입니다. 현재 구상중인 사항으론" +
                    "각 채팅방에서 가장 마지막에 보낸 채팅 + 채팅방 이름 보이도록 생각중입니다.")
    public ApiResponse<TestDTO.TestResponseDTO> getChatRooms(
            @RequestBody TestDTO.TestRequestDTO request,
            @RequestParam Integer page,
            @RequestParam Integer size) {
        return ApiResponse.onSuccess(
                TestDTO.TestResponseDTO.builder()
                        .name(request.getName())
                        .password(request.getPassword())
                        .build());
    }

    @GetMapping("/rooms/{roomId}")
    @Operation(summary = "채팅방 단일 조회",
            description = "")
    public ApiResponse<TestDTO.TestResponseDTO> test3(
            @RequestBody TestDTO.TestRequestDTO request,
            @RequestParam Long roomId
    ) {
        return ApiResponse.onSuccess(
                TestDTO.TestResponseDTO.builder()
                        .name(request.getName())
                        .password(request.getPassword())
                        .build());
    }

    @GetMapping("/rooms/")
    @Operation(summary = "채팅방 만들기 & 채팅 시작 하기 API",
            description = "처음 채팅 시작하기 눌러서 상대를 초대하여 진행되는 채팅방 만들기 API입니다. 이때 동시에 " +
                    "밑에 작성되어 있는 핸드셰이크가 진행되어야 할 듯 합니다.")
    public ApiResponse<TestDTO.TestResponseDTO> test1231232(
            @RequestBody TestDTO.TestRequestDTO request

    ) {
        return ApiResponse.onSuccess(
                TestDTO.TestResponseDTO.builder()
                        .name(request.getName())
                        .password(request.getPassword())
                        .build());
    }

    @GetMapping("/rooms/search/")
    @Operation(summary = "채팅방 검색 API",
            description = "채팅방 검색 기능을 담당할 API입니다.")
    public ApiResponse<TestDTO.TestResponseDTO> searchChatRooms(@RequestBody TestDTO.TestRequestDTO request) {
        return ApiResponse.onSuccess(
                TestDTO.TestResponseDTO.builder()
                        .name(request.getName())
                        .password(request.getPassword())
                        .build());
    }

    @GetMapping("/rooms/{roomId}/pin")
    @Operation(summary = "채팅방 고정 API",
            description = "현재 고민중인 것은 고정된 기록을 db에서 다루어야할지 혹은 프론트 엔드에서 처리가 가능한지 입니다. 논의 후 진행하겠습니다.")
    public ApiResponse<TestDTO.TestResponseDTO> test2(
            @RequestBody TestDTO.TestRequestDTO request,
            @RequestParam Long roomId
    ) {
        return ApiResponse.onSuccess(
                TestDTO.TestResponseDTO.builder()
                        .name(request.getName())
                        .password(request.getPassword())
                        .build());
    }



    @PatchMapping("/rooms/{roomId}")
    @Operation(summary = "채팅방 나가기 API",
            description = "채팅방을 나갈 때 사용되는 API입니다. 양방향에서 진행되는 채팅이기에 한명이 나가도 다른 한명은 존재해야해서" +
                    "boolean형태로 active 상태를 둘거고 해당 상태를 바꾸는 방향으로 설계했습니다.")
    public ApiResponse<TestDTO.TestResponseDTO> getoutofthehere(@RequestBody TestDTO.TestRequestDTO request) {
        return ApiResponse.onSuccess(
                TestDTO.TestResponseDTO.builder()
                        .name(request.getName())
                        .password(request.getPassword())
                        .build());
    }

    //------------------------------------------------------------------------------------------------------------//

    //WS 핸드셰이크를 위한 API

    @GetMapping("/rooms/{roomId}/message")
    @Operation(summary = "채팅 시작 API입니다.",
            description = "채팅방 형성 후 메세지를 처음 보낼 때 해당 API가 사용되어야 소켓 통신을 통한 실시간 채팅으로 전환됩니다.(핸드셰이크)")
    public ApiResponse<TestDTO.TestResponseDTO> handShake(@RequestBody TestDTO.TestRequestDTO request) {
        return ApiResponse.onSuccess(
                TestDTO.TestResponseDTO.builder()
                        .name(request.getName())
                        .password(request.getPassword())
                        .build());
    }




}
