package com.example.momogum.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class LocalDateTImeHolderImpl implements LocalDateTimeHolder{

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
