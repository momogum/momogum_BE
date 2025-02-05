package com.example.momogum.apiPayLoad.exception.handler;

import com.example.momogum.apiPayLoad.code.BaseErrorCode;
import com.example.momogum.apiPayLoad.exception.GeneralException;

public class SearchHandler extends GeneralException {
    public SearchHandler(BaseErrorCode code) {
        super(code);
    }
}
