package com.example.momogum.util;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.handler.TokenHandler;
import com.example.momogum.apiPayLoad.exception.handler.UserEntityHandler;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.web.dto.FcmMessage;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseCloudMessageUtil {

    @Value("${fcm.api-url}")
    private String API_URL;

    private final ObjectMapper objectMapper;
    private final UserEntityRepository userEntityRepository;


    /**
     * FCM 푸시 알림을 전송하는 메서드 입니다
     *
     * 알림을 전송하는 대상의 PK와 알림 제목, 내용을 넣어서 호출해주세요
     * */
    public void sendMessageTo(Long userId, String title, String body) throws IOException {

        UserEntity byId = userEntityRepository.findById(userId)
                .orElseThrow(() -> new UserEntityHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (byId.getFcmToken() == null || byId.getFcmToken().isEmpty()) {
            throw new TokenHandler(ErrorStatus.FCM_TOKEN_NOT_FOUND);
        }

        String message = makeMessage(byId.getFcmToken(), title, body);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json"),
                message
        );

        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            if (!response.isSuccessful()) {
                System.out.println("FCM Error Response: " + responseBody);
                throw new IOException("FCM 메시지 전송 실패: " + response.code());
            }
            System.out.println("FCM Success Response: " + responseBody);
        }
    }

    private String makeMessage(String token, String title, String body) throws JsonParseException, JsonProcessingException {
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                        .token(token)
                        .notification(FcmMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .build()
                        ).build()).validateOnly(false).build();

        return objectMapper.writeValueAsString(fcmMessage);
    }


    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/firebase_service_key.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
