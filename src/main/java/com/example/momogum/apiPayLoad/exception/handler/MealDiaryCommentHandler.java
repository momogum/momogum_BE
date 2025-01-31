package com.example.momogum.apiPayLoad.exception.handler;

import com.example.momogum.apiPayLoad.code.BaseErrorCode;
import com.example.momogum.apiPayLoad.exception.GeneralException;

public class MealDiaryCommentHandler extends GeneralException {
    public MealDiaryCommentHandler(BaseErrorCode code) {
        super(code);
    }
}
