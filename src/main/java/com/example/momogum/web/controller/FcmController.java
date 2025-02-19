package com.example.momogum.web.controller;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.security.CustomUserDetails;
import com.example.momogum.util.FirebaseCloudMessageUtil;
import com.example.momogum.web.dto.FCMRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Tag(name = "FCM 테스트 컨트롤러")
public class FcmController {

    private final FirebaseCloudMessageUtil firebaseCloudMessageService;

    @Operation(summary = "FCM 테스트 API")
    @PostMapping("/api/fcm")
    public ApiResponse<String> pushMessage(
            @AuthenticationPrincipal CustomUserDetails userDetails,  // 현재 로그인한 사용자 정보 가져오기
            @RequestBody FCMRequestDTO requestDTO) throws IOException {

        Long userId = userDetails.getId();

        firebaseCloudMessageService.sendMessageTo(
                userId,
                requestDTO.getTitle(),
                requestDTO.getBody());

        return ApiResponse.onSuccess("성공입니다");
    }
}