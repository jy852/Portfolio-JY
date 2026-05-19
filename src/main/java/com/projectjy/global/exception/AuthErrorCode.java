package com.projectjy.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum AuthErrorCode {

  AUTHENTICATION_REQUIRED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
  EMPTY_TOKEN(HttpStatus.UNAUTHORIZED, "인증 정보가 유효하지 않습니다."),
  INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "인증 정보가 유효하지 않습니다."),
  EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "인증 정보가 유효하지 않습니다."),
  MALFORMED_TOKEN(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
  INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "인증 정보가 유효하지 않습니다."),
  UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "인증 정보가 유효하지 않습니다."),

  ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");

  private final HttpStatus status;
  private final String message;

}
