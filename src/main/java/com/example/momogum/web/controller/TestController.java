package com.example.momogum.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Tag(name = "기술명세서 예시 템플릿 API")
public class TestController {

    @GetMapping("")
    public String test() {
        return "Hello World";
    }
}
