package com.projectjy.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

  UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 에러가 발생하였습니다."),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
  NOT_MATCHES_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.")

  ;
  private final HttpStatus status;
  private final String message;
}
