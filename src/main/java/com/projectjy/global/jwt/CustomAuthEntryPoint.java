package com.projectjy.global.jwt;

import com.projectjy.global.exception.AuthErrorCode;
import com.projectjy.global.exception.JwtTokenException;
import com.projectjy.global.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {

    AuthErrorCode errorCode = resolveErrorCode(authException);

    response.setStatus(errorCode.getStatus().value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());

    ApiResponse<Void> body = ApiResponse.fail(
        errorCode.getStatus(),
        errorCode.getMessage()
    );

    objectMapper.writeValue(response.getWriter(), body);
  }

  private AuthErrorCode resolveErrorCode(AuthenticationException e){
    if(e instanceof JwtTokenException tokenException){
      return tokenException.getError();
    }
    return AuthErrorCode.AUTHENTICATION_REQUIRED;
  }
}
