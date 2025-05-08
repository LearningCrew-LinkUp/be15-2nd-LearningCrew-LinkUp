package com.learningcrew.linkupuser.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    /* 400 BAD_REQUEST: 잘못된 요청 */
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "요청 입력값이 올바르지 않습니다."),

    /* 409 CONFLICT : 서버와의 충돌 */
    NOT_SAVED(HttpStatus.CONFLICT, "데이터 저장에 실패했습니다."),

    /* 404 NOT_FOUND: 리소스를 찾을 수 없음 */
    NOT_FOUND(HttpStatus.NOT_FOUND, "Not found."),


    /* 500 INTERNAL_SERVER_ERROR: 내부 서버 오류 */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),

    // 인증 & 인가 관련
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    EXPIRED_JWT(HttpStatus.UNAUTHORIZED, "JWT 토큰이 만료되었습니다."),
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "잘못된 JWT 토큰입니다."),
    UNSUPPORTED_JWT(HttpStatus.UNAUTHORIZED, "지원하지 않는 JWT 토큰입니다."),
    EMPTY_JWT(HttpStatus.UNAUTHORIZED, "JWT 클레임이 비어있습니다."),


    // 유저
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 등록된 이메일입니다."),
    DUPLICATE_CONTACT_NUMBER(HttpStatus.CONFLICT, "이미 존재하는 번호입니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    INVALID_CREDENTIALS(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호가 올바르지 않습니다."),
    SIGNUP_PENDING(HttpStatus.BAD_REQUEST, "회원가입 신청이 완료된 상태입니다. 이메일 인증을 완료해주세요."), // 명칭+메시지 개선
    INVALID_STATUS(HttpStatus.BAD_REQUEST, "유효하지 않은 상태입니다."),
    INVALID_ROLE(HttpStatus.BAD_REQUEST, "올바르지 않는 권한입니다. "),
    WITHDRAW_USER(HttpStatus.UNAUTHORIZED, "이미 탈퇴한 계정입니다."),
    ACCOUNT_NOT_RECOVERABLE(HttpStatus.BAD_REQUEST, "복구 가능한 계정이 아닙니다 "),
    DUPLICATE_PASSWORD(HttpStatus.BAD_REQUEST, "기존 비밀번호와 동일한 비밀번호는 사용할 수 없습니다. "),
    NOT_FOUND_MANNER_TEMPERATURE(HttpStatus.NOT_FOUND, "매너 온도 정보를 찾을 수 없습니다."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "모임 참여자 정보를 찾을 수 없습니다."),
    NOT_FOUND_STATUS(HttpStatus.NOT_FOUND, "사용자 상태를 찾을 수 없습니다."),

    //친구
    CANNOT_ADD_SELF(HttpStatus.BAD_REQUEST, "자기 자신에게 친구 요청은 불가능합니다."),
    ALREADY_FRIENDED(HttpStatus.BAD_REQUEST, "이미 친구관계가 되어있는 회원입니다. "),
    FRIEND_REQUEST_NOT_FOUND(HttpStatus.BAD_REQUEST, "친구 신청한 대상이 아닙니다"),
    ALREADY_SENT_FRIEND_REQUEST(HttpStatus.BAD_REQUEST, "이미 친구 신청한 대상입니다"),
    ALREADY_REQUESTED_BY_OTHER(HttpStatus.BAD_REQUEST, "이미 회원을 친구 신청한 회원입니다"),

    //포인트
    INSUFFICIENT_POINT(HttpStatus.BAD_REQUEST, "포인트가 부족합니다"),
    NOT_FOUND_POINT(HttpStatus.NOT_FOUND, "포인트 정보를 찾을 수 없습니다."),


    //메일
    FAIL_TO_SEND_EMAIL(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 발송에 실패했습니다."),
    NOT_AUTHORIZED_USER_EMAIL(HttpStatus.BAD_REQUEST, "이메일이 인증되지 않아 회원 가입에 실패했습니다."),
    EXPIRE_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "인증시간이 만료되었습니다. 다시 요청해주세요."),
    INVALID_VERIFICATION_TOKEN(HttpStatus.BAD_REQUEST, "이메일 인증코드가 일치하지 않습니다. "),
    INVALID_TOKEN_TYPE(HttpStatus.BAD_REQUEST, "이메일 타입이 일치하지 않습니다."),


    // 토큰
    REFRESH_TOKEN_MISMATCH(HttpStatus.UNAUTHORIZED, "refresh 토큰이 일치하지 않습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}