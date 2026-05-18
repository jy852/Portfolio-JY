package com.projectjy.global.exception;

import com.projectjy.global.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessException.class) //비즈니스 로직 관련 에러
  public ResponseEntity<ApiResponse<Void>> businessException(BusinessException e){
    return new ResponseEntity<>(ApiResponse.fail(e), e.getErrorStatus());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class) //validation 에러
  public ResponseEntity<ApiResponse<Void>> validationException(MethodArgumentNotValidException e){
    String message = "";
    HttpStatus status = HttpStatus.BAD_REQUEST;

    if(e.getBindingResult().getFieldError() != null){
      message = e.getBindingResult().getFieldError().getDefaultMessage();
    }

    return new ResponseEntity<>(ApiResponse.fail(status, message), status);
  }

  @ExceptionHandler(Exception.class) //그 외의 알 수 없는 에러 (런타임 에러)
  public ResponseEntity<ApiResponse<Void>> exception(Exception e){
    log.error("알 수 없는 에러: {}", e.getMessage());
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    return new ResponseEntity<>(ApiResponse.fail(new BusinessException(ErrorCode.UNKNOWN_ERROR)), status);
  }
}
