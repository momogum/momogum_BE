package com.example.momogum.apiPayLoad.exception.handler;

import com.example.momogum.apiPayLoad.code.BaseErrorCode;
import com.example.momogum.apiPayLoad.exception.GeneralException;

public class MealDiaryStoryHandler extends GeneralException {
    public MealDiaryStoryHandler(BaseErrorCode code) {
        super(code);
    }
}
