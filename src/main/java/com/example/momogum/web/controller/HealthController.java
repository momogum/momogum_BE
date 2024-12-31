package com.example.momogum.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@Tag(name = "서버 안정성 체크 API")
public class HealthController {

    @GetMapping
    public String healthCheck() {
        return "OK";
    }
}
