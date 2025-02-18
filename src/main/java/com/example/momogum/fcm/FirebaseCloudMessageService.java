package com.example.momogum.fcm;

import com.example.momogum.apiPayLoad.code.status.ErrorStatus;
import com.example.momogum.apiPayLoad.exception.handler.UserEntityHandler;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.repository.userEntityRepo.UserEntityRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseCloudMessageService {

    private final String API_URL = "https://fcm.googleapis.com/v1/projects/" +
            "momogum-d9bfb/messages:send";
    private final ObjectMapper objectMapper;
    private final UserEntityRepository userEntityRepository;

    public void sendMessageTo(Long userId, String title, String body) throws IOException {

        log.info("------------푸시알림 시작------------");

        UserEntity byId = userEntityRepository.findById(userId)
                .orElseThrow(() -> new UserEntityHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (byId.getFcmToken() == null || byId.getFcmToken().isEmpty()) {
            throw new IllegalStateException("FCM 토큰이 없습니다.");
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
                System.out.println("FCM Error Response: " + responseBody);  // 에러 응답 로깅
                throw new IOException("FCM 메시지 전송 실패: " + response.code());
            }
            System.out.println("FCM Success Response: " + responseBody);  // 성공 응답 로깅
        }
    }
    private String makeMessage(String targetToken, String title, String body) throws JsonProcessingException {
        FcmMessage fcmMessage = FcmMessage.builder()
                .validateOnly(false)
                .message(FcmMessage.Message.builder()
                        .token(targetToken)
                        .notification(FcmMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build())
                        .build())
                .build();

        // ObjectMapper 설정 추가
        ObjectMapper mapper = new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String jsonMessage = mapper.writeValueAsString(fcmMessage);
        System.out.println("Generated FCM message: " + jsonMessage);  // 디버깅용
        return jsonMessage;
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
