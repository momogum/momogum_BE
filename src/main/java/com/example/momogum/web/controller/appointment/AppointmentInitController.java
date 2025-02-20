package com.example.momogum.web.controller.appointment;

import com.example.momogum.apiPayLoad.ApiResponse;
import com.example.momogum.domain.appointment.Appointment;
import com.example.momogum.service.appointmentService.AppointmentService;
import com.example.momogum.web.dto.appointment.AppointmentDTO.CreateAppointmentResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appointment")
@Tag(name = "약속잡기 초기 API")
@RequiredArgsConstructor
public class AppointmentInitController {

    private final AppointmentService appointmentService;

    @PostMapping("/init")
    public ApiResponse<CreateAppointmentResponseDTO> initAppointment() {
        Appointment appointment = appointmentService.createTemporaryAppointment();

        return ApiResponse.onSuccess(new CreateAppointmentResponseDTO(appointment.getId()));
    }
}
