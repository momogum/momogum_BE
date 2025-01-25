package com.example.momogum.apiPayLoad.exception.handler;

import com.example.momogum.apiPayLoad.code.BaseErrorCode;
import com.example.momogum.apiPayLoad.exception.GeneralException;

public class UserEntityHandler extends GeneralException {
    public UserEntityHandler(BaseErrorCode code) {
        super(code);
    }
}
