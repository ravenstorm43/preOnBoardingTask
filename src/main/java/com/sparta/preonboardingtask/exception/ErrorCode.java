package com.sparta.preonboardingtask.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    FAIL(500, "실패했습니다."),
    NOT_UNAUTHORIZED(403, "권한이 없습니다."),
    UNAUTHENTICATED(401, "로그인 후 이용해주세요."),
    UNAUTHORIZED_MANAGER(403, "매니저가 아닙니다."),
    INVALID_REQUEST(400, "잘못 된 요청입니다."),
    INCORRECT_PASSWORD(400, "입력하신 비밀번호가 일치하지 않습니다."),
    INCORRECT_MANAGER_KEY(400, "입력하신 MANAGER키가 일치하지 않습니다."),
    CHECK_USERNAME(400, "아이디를 올바르게 입력하셨는지 확인해주세요."),
    INVALID_URL_ACCESS(400, "잘못된 URL 접근입니다."),
    USER_NOT_FOUND(400, "해당하는 유저를 찾을 수 없습니다."),
    USER_NOT_UNIQUE(409,"사용 중인 아이디입니다."),
    TOKEN_EXPIRED(401, "토큰이 만료되었습니다."),
    INVALID_TOKEN(401, "잘못된 JWT 토큰입니다."),
    TOKEN_MISMATCH(401, "토큰이 일치하지 않습니다."),
    RE_LOGIN_REQUIRED(401, "재로그인 해주세요"),
    USER_WITHDRAW(403, "이미 탈퇴한 회원입니다."),

    NAME_NOT_UNIQUE(409,"사용 중인 닉네임입니다."),
    USER_LOGOUT(403, "이미 로그아웃한 회원입니다."),
    TOKEN_NOTFOUND(404, "토큰을 찾을 수 없습니다.");

    private final Integer status;
    private final String message;
}