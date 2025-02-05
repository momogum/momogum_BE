package com.example.momogum.apiPayLoad.exception;


import com.example.momogum.apiPayLoad.code.status.ErrorStatus;

public class DuplicateUserException extends RuntimeException {
    private final ErrorStatus errorStatus;

    public DuplicateUserException(String message) {
        super(message);
        this.errorStatus = ErrorStatus.DUPLICATE_PROVIDER_ID; // 적절한 에러 상태 코드
    }

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }
}
