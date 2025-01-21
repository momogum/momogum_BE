package com.example.momogum.apiPayLoad.code.status;

import com.example.momogum.apiPayLoad.code.BaseErrorCode;
import com.example.momogum.apiPayLoad.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    MEMBER_IMAGE_EXIST(HttpStatus.BAD_REQUEST,"IMAGE4001","프로필 이미지가 이미 등록되어 있습니다"),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND,"IMAGE4002","이미지를 찾을 수 없습니다"),
    IMAGE_CONVERT_ERROR(HttpStatus.SERVICE_UNAVAILABLE,"IMAGE5001","이미지 변환 중 오류가 발생하였습니다"),
    IMAGE_REMOVE_ERROR(HttpStatus.SERVICE_UNAVAILABLE,"IMAGE5002","이미지 삭제 중 오류가 발생하였습니다"),
    IMAGE_DOWNLOAD_ERROR(HttpStatus.SERVICE_UNAVAILABLE,"IMAGE5003","이미지 다운로드 중 오류가 발생하였습니다");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}