package com.example.momogum.apiPayLoad.exception.handler;

import com.example.momogum.apiPayLoad.code.BaseErrorCode;
import com.example.momogum.apiPayLoad.exception.GeneralException;

public class ImageHandler extends GeneralException {
    public ImageHandler(BaseErrorCode code) {
        super(code);
    }
}
