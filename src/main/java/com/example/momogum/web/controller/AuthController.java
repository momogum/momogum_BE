package com.example.momogum.web.controller;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.domain.UserEntity;
import com.example.momogum.service.TokenService;
import com.example.momogum.web.dto.AuthDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(name = "소셜 로그인 및 인증 API", description = "카카오, 애플 등의 소셜 로그인 및 회원 정보 관련 API")
public class AuthController {

    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * 카카오 로그인 API
     * 클라이언트가 제공한 카카오 인증 토큰을 검증하고, 유저 정보를 가져와 JWT를 발급합니다.
     */
    @Operation(summary = "카카오 로그인 API", description = "카카오 소셜 로그인 요청을 처리하고 JWT를 발급합니다.")
    @PostMapping("/login/kakao")
    public ApiResponse<AuthDTO.TokenResponseDTO> kakaoLogin(@RequestBody AuthDTO.AuthRequestDTO request) {
        log.info("받은 액세스 토큰: {}", request.getAccessToken());
        String kakaoAccessToken = request.getAccessToken();

        HttpServletRequest requests = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String clientIp = requests.getRemoteAddr();
        log.info("클라이언트 IP: {}", clientIp);

        // 사용자 정보 저장 또는 업데이트
        UserEntity user = tokenService.processUserLogin(kakaoAccessToken);

        // JWT 토큰 생성
        String accessToken = tokenService.createAccessToken(user.getId().toString());
        String refreshToken = tokenService.createRefreshToken(user.getId().toString());

        // Redis에 토큰 저장
        tokenService.saveTokens(accessToken, refreshToken, user.getId().toString());

        // 응답 반환
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
    @GetMapping("/callback/kakao")
    @Operation(summary = "카카오 OAuth 콜백", description = "카카오 인증 서버에서 리디렉션된 요청을 처리합니다.")
    public ResponseEntity<String> kakaoCallback(@RequestParam String code) {
        System.out.println("카카오 인증 코드: " + code);

        // 인증 코드를 통해 액세스 토큰 요청
        String accessToken = tokenService.requestAccessTokenFromKakao(code);

        // 사용자 로그인 처리 (토큰 저장 등)
        UserEntity user = tokenService.processUserLogin(accessToken);

        // 클라이언트에 처리된 결과 반환 (임시)
        return ResponseEntity.ok("카카오 인증 및 사용자 처리 완료. 사용자: " + user.getNickname());
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
    public ApiResponse<AuthDTO.AuthResponseDTO> getUserInfo(@PathVariable Long userId) {
        // FIXME: 실제 DB에서 조회하는 로직 추가 필요
        return ApiResponse.onSuccess(
                AuthDTO.AuthResponseDTO.builder()
                        .name("머머금")
                        .provider("kakao")
                        .build()
        );
    }
}
