package com.projectjy.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

@Getter
public class JwtTokenException extends AuthenticationException {

  private final AuthErrorCode error;

  public JwtTokenException(AuthErrorCode err) {
    super(err.getMessage());
    this.error = err;
  }
}
