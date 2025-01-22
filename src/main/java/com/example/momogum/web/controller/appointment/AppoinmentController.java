package com.example.momogum.web.controller.appointment;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.web.dto.user.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.example.momogum.web.dto.appointment.AppointMentDTO.*;
import static com.example.momogum.web.dto.user.UserDTO.*;

@RestController
@RequestMapping("/Appointment")
@Tag(name = "약속잡기 관련 API")
public class AppoinmentController {

    /**
     * 1. 추가된 사람들 프로필 조회 API
     */
    @Operation(summary = "추가된 사람들 프로필 조회 API",
            description = "특정 모임에 추가된 유저의 프로필 목록을 반환합니다.")
    @GetMapping("appointmentId/{appointmentId}/profiles")
    public ApiResponse<List<UserResponseDTO>> getProfiles(@PathVariable Long appointmentId) {
        // API 구현 시 수정 예정 FIXME
        //MealPlan과 User 엔티티 연관관계 맺고 있다고 가정
        // mealPlanId로 해당 mealPlan에 연관되어있는 user를 조회합니다.

        return ApiResponse.onSuccess(List.of(
                UserResponseDTO.builder()
                        .id(1L)
                        .name("머머금")
                        .nickname("머머")
                        .profileImage("/path/to/image")
                        .build()
        ));
    }


    /**
     * 2. 모임에 유저 추가 API
     */
    @Operation(summary = "모임에 사람 추가 API",
            description = "특정 모임에 유저를 추가합니다.")
    @PostMapping("appointmentId/{appointmentId}/profiles")
    public ApiResponse<List<UserResponseDTO>> addProfile(@PathVariable Long appointmentId, @RequestBody Long userId) {
        // API 구현 시 수정 예정 FIXME
        //약속잡기 페이지에서 user를 +버튼으로 추가합니다.
        return ApiResponse.onSuccess(List.of(
                UserResponseDTO.builder()
                        .id(1L) //userId
                        .name("머머금")
                        .nickname("머머")
                        .profileImage("/path/to/image")
                        .build()
        ));
    }

    /**
     * 3. 모임 정보 저장
     */
    @Operation(summary = "모임 정보 저장 API",
            description = "모임의 세부 정보를 JSON 형식으로 저장합니다.")
    @PostMapping("/create")
    public ApiResponse<CreateAppointmentResponseDTO> createMealPlan(@RequestBody AppointmentResponseDTO appointment) {
        // API 구현 시 수정 예정 FIXME
        return ApiResponse.onSuccess(
                CreateAppointmentResponseDTO.builder()
                        .mealPlanId(1L)
                        .build());
    }

    /**
     * 4. 모임 정보 조회
     */
    @Operation(summary = "모임 정보 확인",
            description = "특정 모임의 세부 정보를 반환합니다.")
    @GetMapping("appointmentId/{appointmentId}")
    public ApiResponse<AppointmentResponseDTO> getMealPlan(@PathVariable Long appointmentId) {
        // API 구현 시 수정 예정 FIXME
        //mealPlanId를 통해 해당 mealPlan을 repository에서 조회할 예정입니다.
        return ApiResponse.onSuccess(
                AppointmentResponseDTO.builder()
                        .id(appointmentId)
                        .title("커피 한 잔 잡쉈어?")
                        .menu("브런치")
                        .date(LocalDate.of(2025, 1, 10))
                        .location("한양대학로 55")
                        .notes("10시까지 오세요.")
                        .createdAt("2025-01-10T12:00:00")
                        .users(List.of(
                                UserDTO.UserResponseDTO.builder()
                                        .id(1L)
                                        .name("머머금")
                                        .nickname("머머")
                                        .profileImage("/path/to/image")
                                        .build()
                        ))
                        .build());
    }

    /**
     * 5. 모임 삭제
     */
    @Operation(summary = "모임 삭제 API",
            description = "특정 모임을 삭제합니다.")
    @DeleteMapping("appointmentId/{appointmentId}")
    public ApiResponse<String> deleteMealPlan(@PathVariable Long appointmentId) {
        return ApiResponse.onSuccess("특정 모임을 삭제합니다");
    }

}
