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
    CONFLICT(HttpStatus.CONFLICT, "COMMON409", "요청이 충돌합니다."),


    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "MEMBER4003", "이미 사용 중인 닉네임입니다"),
    DUPLICATE_PROVIDER_ID(HttpStatus.CONFLICT, "MEMBER4004", "이미 등록된 providerId가 있습니다"),
    MEMBER_AUTHENTICATE_FAILED(HttpStatus.BAD_REQUEST,"MEMBER4002","회원인증에 실패하였습니다"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"MEMBER4001","회원을 찾을 수 없습니다"),

    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"COMMENT4001","댓글을 찾을 수 없습니다"),

    MEALDIARY_REPORTED(HttpStatus.BAD_REQUEST,"MEALDIARY4003","이미 신고 접수된 게시글 입니다"),
    MEALDIARY_KEYWORD_MAX(HttpStatus.BAD_REQUEST,"MEALDIARY4002","밥일기의 키워드는 최대 다섯개만 입력 할 수 있습니다"),
    MEALDIARY_NOT_FOUND(HttpStatus.NOT_FOUND,"MEALDIARY4001","밥일기를 찾을 수 없습니다"),

    MEMBER_IMAGE_EXIST(HttpStatus.BAD_REQUEST,"IMAGE4001","프로필 이미지가 이미 등록되어 있습니다"),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND,"IMAGE4002","이미지를 찾을 수 없습니다"),
    IMAGE_CONVERT_ERROR(HttpStatus.SERVICE_UNAVAILABLE,"IMAGE5001","이미지 변환 중 오류가 발생하였습니다"),
    IMAGE_REMOVE_ERROR(HttpStatus.SERVICE_UNAVAILABLE,"IMAGE5002","이미지 삭제 중 오류가 발생하였습니다"),
    IMAGE_UPLOAD_ERROR(HttpStatus.BAD_GATEWAY,"IMAGE5004","이미지 등록 중 오류가 발생하였습니다"),
    IMAGE_DOWNLOAD_ERROR(HttpStatus.SERVICE_UNAVAILABLE,"IMAGE5003","이미지 다운로드 중 오류가 발생하였습니다"),

    USER_PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "USER4001", "유저 프로필을 찾을 수 없습니다"),
    INVALID_NICKNAME_FORMAT(HttpStatus.BAD_REQUEST, "USER4002", "닉네임은 5~20자의 영어 소문자, 숫자, 특수문자만 가능합니다."),
    INVALID_NAME_FORMAT(HttpStatus.BAD_REQUEST,"USER4003", "이름은 1~12자의 한글 또는 영문만 가능합니다."),
    INVALID_ABOUT_LENGTH(HttpStatus.BAD_REQUEST, "USER4004", "한 줄 소개는 최대 40자까지 입력 가능합니다"),
    PROFILE_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "USER4005", "프로필 이미지를 찾을 수 없습니다"),
    PROFILE_IMAGE_UPLOAD_FAILED(HttpStatus.BAD_GATEWAY, "USER4006", "프로필 이미지 업로드에 실패하였습니다"),
    PROFILE_IMAGE_DELETE_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "USER4007", "프로필 이미지 삭제에 실패하였습니다"),
    PROFILE_UPDATE_FAILED(HttpStatus.BAD_REQUEST, "USER4008", "유저 프로필 업데이트에 실패하였습니다"),

    TARGET_NOT_FOUND(HttpStatus.NOT_FOUND,"FOLLOW5001","대상을 찾을 수 없습니다"),

    //search에 사용
    KEYWWORD_BLANK(HttpStatus.BAD_REQUEST,"SEARCH4001","검색어는 필수입니다."),
    ;


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