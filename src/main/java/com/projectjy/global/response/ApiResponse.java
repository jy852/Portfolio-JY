package com.projectjy.global.response;

import com.projectjy.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public record ApiResponse<T>(
    int status,
    String message,
    T data
) {

  public static <T> ApiResponse<T> ok(String message){
    return new ApiResponse<>(HttpStatus.OK.value(), message, null);
  }

  public static <T> ApiResponse<T> created(String message, T data){
    return new ApiResponse<>(HttpStatus.CREATED.value(), message, data);
  }

  public static ApiResponse<Void> fail(HttpStatus status, String message){
    return new ApiResponse<>(status.value(), message, null);
  }

  public static ApiResponse<Void> fail(BusinessException e){
    return new ApiResponse<>(e.getErrorStatus().value(), e.getMessage(), null);
  }
}
