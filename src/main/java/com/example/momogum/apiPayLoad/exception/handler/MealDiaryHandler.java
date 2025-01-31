package com.example.momogum.apiPayLoad.exception.handler;

import com.example.momogum.apiPayLoad.code.BaseErrorCode;
import com.example.momogum.apiPayLoad.exception.GeneralException;

public class MealDiaryHandler extends GeneralException {
    public MealDiaryHandler(BaseErrorCode code) {
        super(code);
    }
}
