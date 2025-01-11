package com.example.momogum.web.controller;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.web.dto.AuthDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "소셜 로그인 및 인증 API", description = "카카오, 애플 등의 소셜 로그인 및 회원 정보 관련 API")
public class AuthController {

    /**
     * 1. 카카오 로그인 API
     * 카카오 로그인 요청을 받아 처리하는 역할을 하는 API입니다.
     * 클라이언트에게 받은 카카오 로그인 인증 토큰을 검증한 뒤, 카카오 서버에서 유저 정보를 가져옵니다. 이때 가져온 유저 정보를 기반으로 회원가입 혹은 로그인 처리를 합니다.
     */
    @Operation(summary = "카카오 로그인 API", description = "카카오 소셜 로그인 요청을 처리합니다.")
    @PostMapping("/login/kakao")
    public ApiResponse<AuthDTO.AuthResponseDTO> kakaoLogin(@RequestBody AuthDTO.AuthRequestDTO request) {
        // Redis로 토큰 관리하는 로직으로 변경
        return ApiResponse.onSuccess(
                AuthDTO.AuthResponseDTO.builder()
                        .email("kakaoUser@example.com")
                        .name("Kakao User")
                        .provider("kakao")
                        .build()
        );
    }

    /**
     * 2. 애플 로그인 API
     * 카카오 로그인부터 모두 구현되고 나서, 출시를 기준으로 작업이 시작될 때 작업할 예정입니다.
     * 작업 절차는 카카오 로그인과 거의 같습니다. 사용자가 로그인 버튼을 클릭하면 애플 서버에서 인증을 수행하고,
     * 인증이 완료되면 클ㄹ라이언트는 서버로부터 받아온 토큰을 이 API에 전달합니다. 가져온 유저 정보를 기반으로 회원가입 또는 로그인 처리를 합니다.
     *
     * [참고사항]
     * 애플 로그인은 카카오 로그인에 비해 정책 및 보안 요건에 맞는 추가 작업이 필요
     */
    @Operation(summary = "애플 로그인 API", description = "애플 소셜 로그인 요청을 처리합니다.")
    @PostMapping("/login/apple")
    public ApiResponse<AuthDTO.AuthResponseDTO> appleLogin(@RequestBody AuthDTO.AuthRequestDTO request) {
        return ApiResponse.onSuccess(
                AuthDTO.AuthResponseDTO.builder()
                        .email("appleUser@example.com")
                        .name("Apple User")
                        .provider("apple")
                        .build()
        );
    }

    /**
     * 3. 회원 정보 조회 API
     * 클라이언트가 회원의 식별자를 전달하면, 해당 회원의 정보를 반환하는 회원 정보 확인용 API 입니다.
     */
    @Operation(summary = "회원 정보 조회 API", description = "특정 회원의 정보를 반환합니다.")
    @GetMapping("/users/{userId}")
    public ApiResponse<AuthDTO.AuthResponseDTO> getUserInfo(@PathVariable Long userId) {
        return ApiResponse.onSuccess(
                AuthDTO.AuthResponseDTO.builder()
                        .email("user@example.com")
                        .name("머머금")
                        .provider("kakao")
                        .build()
        );
    }
}
