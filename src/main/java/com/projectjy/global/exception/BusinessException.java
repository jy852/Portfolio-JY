package com.projectjy.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

  private final ErrorCode error;

  public BusinessException(ErrorCode err) {
    super(err.getMessage());
    this.error = err;
  }

  public HttpStatus getErrorStatus(){
    return error.getStatus();
  }

}
