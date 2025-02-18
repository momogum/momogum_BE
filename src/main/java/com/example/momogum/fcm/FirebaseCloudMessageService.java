package com.example.momogum.fcm;

import com.example.momogum.domain.UserEntity;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.example.momogum.service.UserService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FirebaseCloudMessageService {

    private final UserEntityRepository userEntityRepository;

    public String sendMessage(FcmMessageRequestDto requestDto) {
        // 사용자의 Firebase 토큰 값을 조회
        UserEntity findUser = userEntityRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("회원을 찾지 못했습니다"));

        String fcmToken = findUser.getFcmToken();

        // 메시지 구성
        Message message = Message.builder()
                .putData("title", requestDto.getTitle())
                .putData("content", requestDto.getBody())
                .setToken(fcmToken) // 조회한 토큰 값을 사용
                .build();

        try {
            // 메시지 전송
            String response = FirebaseMessaging.getInstance().send(message);
            return "메세지 전송에 성공하였습니다: " + response;
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            return "메세지 전송에 실패하였습니다";
        }
    }
}