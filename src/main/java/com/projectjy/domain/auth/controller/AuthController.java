package com.projectjy.domain.auth.controller;

import com.projectjy.domain.auth.dto.request.LoginRequest;
import com.projectjy.domain.auth.dto.response.LoginResponse;
import com.projectjy.domain.auth.serivce.AuthService;
import com.projectjy.global.response.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<Void>> login(
      @RequestBody @Valid LoginRequest request,
      HttpServletResponse response
  ){

    LoginResponse loginResponse = authService.login(request);

    Cookie cookie = new Cookie("refreshToken", loginResponse.refreshToken());
    cookie.setMaxAge(60 * 60 * 24);
    cookie.setHttpOnly(true);
    cookie.setPath("/");


    response.setHeader("Authorization", "Bearer " + loginResponse.accessToken());
    response.addCookie(cookie);

    return ResponseEntity.ok(ApiResponse.ok("로그인에 성공하였습니다."));
  }



}
