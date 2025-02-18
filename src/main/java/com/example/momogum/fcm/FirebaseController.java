package com.example.momogum.fcm;

import com.example.momogum.apiPayLoad.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FirebaseController {

    private final FirebaseCloudMessageService firebaseMessageService;

    @Operation(summary = "fcm 테스트 API")
    @PostMapping("/fcm/sendMessage")
    public ApiResponse<String> sendMessage(@RequestBody FcmMessageRequestDto requestDto) {
        String response = firebaseMessageService.sendMessage(requestDto);
        return ApiResponse.onSuccess(response);
    }
}
