package com.example.momogum.web.controller;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.service.TokenService;
import com.example.momogum.web.dto.AuthDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

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
        String accessToken = tokenService.createAccessToken(request.getName());
        String refreshToken = tokenService.createRefreshToken(request.getName());

        tokenService.saveTokens(accessToken, refreshToken, request.getName());

        return ApiResponse.onSuccess(
                AuthDTO.TokenResponseDTO.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build()
        );
    }

    /**
     * 애플 로그인 API
     * 클라이언트가 제공한 애플 인증 토큰을 검증하고, 유저 정보를 가져와 JWT를 발급합니다.
     */
    @Operation(summary = "애플 로그인 API", description = "애플 소셜 로그인 요청을 처리하고 JWT를 발급합니다.")
    @PostMapping("/login/apple")
    public ApiResponse<AuthDTO.TokenResponseDTO> appleLogin(@RequestBody AuthDTO.AuthRequestDTO request) {
        String accessToken = tokenService.createAccessToken(request.getName());
        String refreshToken = tokenService.createRefreshToken(request.getName());

        tokenService.saveTokens(accessToken, refreshToken, request.getName());

        return ApiResponse.onSuccess(
                AuthDTO.TokenResponseDTO.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build()
        );
    }

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
                        .email("user@example.com")
                        .name("머머금")
                        .provider("kakao")
                        .build()
        );
    }
}
