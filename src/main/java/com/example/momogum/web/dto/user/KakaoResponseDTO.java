package com.example.momogum.web.dto.user;

import lombok.Getter;
import lombok.Setter;


// 카카오 API의 JSON 응답을 매핑하기 위한 DTO입니다. 클라이언트와 직접적으로 주고받는 것이 아닌, 외부 API와의 통신에서  사용됩니다.
@Getter
@Setter
public class KakaoResponseDTO {
    private String id; // 카카오 사용자 ID
    private KakaoAccount kakao_account;

    @Getter
    @Setter
    public static class KakaoAccount {
        private KakaoProfile profile;
        private String email; // 사용자 이메일

        @Getter
        @Setter
        public static class KakaoProfile {
            private String nickname; // 사용자 닉네임
            private String profile_image_url; // 프로필 이미지 URL
        }
    }
}
