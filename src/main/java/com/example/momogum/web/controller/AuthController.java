package com.example.momogum.web.controller;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.service.TokenService;
import com.example.momogum.service.UserService;
import com.example.momogum.web.dto.AuthDTO;
import com.example.momogum.web.dto.user.KakaoResponseDTO;
import com.example.momogum.web.dto.user.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Tag(name = "소셜 로그인 및 인증 API", description = "카카오, 애플 등의 소셜 로그인 및 회원 정보 관련 API")
public class AuthController {
    private final UserService userService;
    private final TokenService tokenService;


    /**
     * 카카오 로그인 API
     * 클라이언트가 제공한 카카오 인증 토큰을 검증하고, 신규 유저인 경우 추가 입력 단계로 진행합니다.
     */
    @Operation(summary = "카카오 로그인 API", description = "기존 유저가 요청하는 경우 카카오 로그인 처리 후 토큰값을 반환합니다. 신규 유저가 요청하는 경우 isNewUser를 true값으로 반환합니다.")
    @PostMapping("/login/kakao")
    public ApiResponse<?> kakaoLogin(@RequestBody AuthDTO.AuthRequestDTO request) {
        log.info("받은 액세스 토큰: {}", request.getAccessToken());
        String kakaoAccessToken = request.getAccessToken();

        // 카카오에서 유저 정보를 가져오기
        KakaoResponseDTO kakaoResponseDTO = tokenService.fetchKakaoUserInfo(kakaoAccessToken);

        // providerId를 추출
        String providerId = kakaoResponseDTO.getId();
        log.info("provider id 값 체크: {}" , providerId);
        // 사용자 존재 여부 확인
        boolean isExistingUser = tokenService.isExistingUserByProviderId(providerId);

        if (isExistingUser) {
            // 기존 사용자 처리
            UserEntity user = tokenService.processExistingUserLoginByProviderId(providerId);

            // JWT 토큰 생성
            String accessToken = tokenService.createAccessToken(user.getId().toString());
            String refreshToken = tokenService.createRefreshToken(user.getId().toString());

            // Redis에 토큰 저장
            tokenService.saveTokens(accessToken, refreshToken, user.getId().toString());

            // JWT 토큰 응답 반환
            return ApiResponse.onSuccess(
                    AuthDTO.TokenResponseDTO.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build()
            );
        } else {
            // 신규 사용자 처리 (추가 정보 입력 필요)
            return ApiResponse.onSuccess(
                    UserDTO.UserResponseDTO.builder()
                            .id(null)
                            .name(kakaoResponseDTO.getKakao_account().getProfile().getNickname())
                            .nickname(null)
                            .profileImage(kakaoResponseDTO.getKakao_account().getProfile().getProfile_image_url())
                            .isNewUser(true)
                            .build()
            );
        }
    }

    /**
     * 닉네임 중복 확인 API
     * 사용자가 입력한 닉네임이 기존에 존재하는지 확인합니다.
     */
    @Operation(summary = "닉네임 중복 확인 API", description = "입력한 닉네임이 중복인지 여부를 반환합니다.")
    @GetMapping("/check-nickname")
    public ApiResponse<Boolean> checkNickname(@RequestParam String nickname) {
        log.info("닉네임 중복 확인 요청 - 닉네임: {}", nickname);
        boolean isDuplicate = userService.isNicknameDuplicate(nickname);
        return ApiResponse.onSuccess(isDuplicate);
    }


    // 프론트 단에서, kakaoLogin의 응답으로 신규 유저임을 확인받은 경우, 이 경로로 요청을 보내게 됩니다.
    @Operation(summary = "신규 사용자 추가 API", description = "신규 사용자 정보 입력 후 DB에 저장합니다.")
    @PostMapping("/signup/kakao")
    public ApiResponse<AuthDTO.TokenResponseDTO> kakaoSignUp(@RequestBody AuthDTO.SignUpRequestDTO request) {
        log.info("신규 사용자 정보 입력 요청 - 이름: {}, 닉네임: {}", request.getName(), request.getNickname());

        // 카카오 API에서 유저 정보 가져오기
        KakaoResponseDTO kakaoResponseDTO = tokenService.fetchKakaoUserInfo(request.getAccessToken());
        String providerId = kakaoResponseDTO.getId(); // providerId 추출

        // 신규 사용자 처리
        UserEntity user = tokenService.processNewUserLogin(
                providerId,                 // 카카오에서 추출한 providerId
                request.getName(),          // 입력된 이름
                request.getNickname(),      // 입력된 닉네임
                kakaoResponseDTO.getKakao_account().getProfile().getProfile_image_url() // 프로필 이미지
        );

        // JWT 토큰 생성
        String accessToken = tokenService.createAccessToken(user.getId().toString());
        String refreshToken = tokenService.createRefreshToken(user.getId().toString());

        // Redis에 토큰 저장
        tokenService.saveTokens(accessToken, refreshToken, user.getId().toString());

        // JWT 응답 반환
        return ApiResponse.onSuccess(
                AuthDTO.TokenResponseDTO.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build()
        );
    }


    /**
     * 카카오 리디렉션 URI 처리 API
     * 카카오 인증 서버에서 리디렉션된 요청을 처리합니다.
     */
    @Operation(summary = "카카오 OAuth 콜백", description = "카카오 인증 서버에서 리디렉션된 요청을 처리합니다.")
    @GetMapping("/callback/kakao")
    public ApiResponse<?> kakaoCallback(@RequestParam String code) {
        log.info("카카오 인증 코드: {}", code);

        // 인증 코드를 통해 액세스 토큰 요청
        String providerId = tokenService.requestAccessTokenFromKakao(code);

        // 유저가 기존 유저인지 여부 확인
        boolean isExistingUser = tokenService.isExistingUserByProviderId(providerId);

        if (isExistingUser) {
            // 기존 사용자 처리
            UserEntity user = tokenService.processExistingUserLoginByProviderId(providerId);

            // JWT 토큰 생성
            String accessTokenJWT = tokenService.createAccessToken(user.getId().toString());
            String refreshTokenJWT = tokenService.createRefreshToken(user.getId().toString());

            // Redis에 토큰 저장
            tokenService.saveTokens(accessTokenJWT, refreshTokenJWT, user.getId().toString());

            // 기존 사용자용 응답 반환
            return ApiResponse.onSuccess(
                    AuthDTO.TokenResponseDTO.builder()
                            .accessToken(accessTokenJWT)
                            .refreshToken(refreshTokenJWT)
                            .build()
            );
        } else {
            // 신규 사용자 처리
            return ApiResponse.onSuccess(
                    UserDTO.UserResponseDTO.builder()
                            .id(null)
                            .name("신규 사용자 이름") // 기본값
                            .profileImage("신규 프로필 이미지") // 기본값
                            .isNewUser(true)
                            .build()
            );
        }
    }



    /**
     * 애플 로그인 API
     * 클라이언트가 제공한 애플 인증 토큰을 검증하고, 유저 정보를 가져와 JWT를 발급합니다.
     */
//    @Operation(summary = "애플 로그인 API", description = "애플 소셜 로그인 요청을 처리하고 JWT를 발급합니다.")
//    @PostMapping("/login/apple")
//    public ApiResponse<AuthDTO.TokenResponseDTO> appleLogin(@RequestBody AuthDTO.AuthRequestDTO request) {
//        String accessToken = tokenService.createAccessToken(request.getName());
//        String refreshToken = tokenService.createRefreshToken(request.getName());
//
//        tokenService.saveTokens(accessToken, refreshToken, request.getName());
//
//        return ApiResponse.onSuccess(
//                AuthDTO.TokenResponseDTO.builder()
//                        .accessToken(accessToken)
//                        .refreshToken(refreshToken)
//                        .build()
//        );
//    }

    /**
     * 회원 정보 조회 API
     * 클라이언트가 회원의 식별자를 전달하면, 해당 회원의 정보를 반환합니다.
     */
    @Operation(summary = "회원 정보 조회 API", description = "특정 회원의 정보를 반환합니다.")
    @GetMapping("/users/{userId}")
    public ApiResponse<UserDTO.UserResponseDTO> getUserInfo(@PathVariable Long userId) {
        log.info("회원 정보 조회 요청 - 유저 ID: {}", userId);

        // 데이터베이스에서 회원 정보 조회
        UserEntity user = tokenService.findUserById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));

        // 프로필 이미지 URL 가져오기
        String profileImageUrl = user.getProfileImage() != null ? user.getProfileImage().getImageLink() : null;

        // 응답 생성 및 반환
        return ApiResponse.onSuccess(
                UserDTO.UserResponseDTO.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .nickname(user.getNickname())
                        .profileImage(profileImageUrl)  // 프로필 이미지 URL 반환
                        .isNewUser(false) // 회원 조회는 항상 기존 사용자임
                        .build()
        );
    }



}
