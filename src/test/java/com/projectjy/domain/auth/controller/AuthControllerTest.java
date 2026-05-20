package com.projectjy.domain.auth.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.projectjy.domain.auth.dto.request.LoginRequest;
import com.projectjy.domain.auth.dto.response.LoginResponse;
import com.projectjy.domain.auth.serivce.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectjy.global.jwt.JWTAuthenticationFilter;
import com.projectjy.global.jwt.JWTProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = AuthController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = JWTAuthenticationFilter.class
    )
)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private AuthService authService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @DisplayName("로그인 성공 시 AT, RT응답값 JSON 테스트")
  void successful_login_controller_mvc_test() throws Exception {
    //given
    LoginRequest request = new LoginRequest("username", "password");

    //when
    Mockito.when(authService.login(request)).thenReturn(new LoginResponse("access-token", "refresh-token"));

    //then
    mockMvc.perform(post("/api/v1/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(header().string(HttpHeaders.AUTHORIZATION, "Bearer access-token"))
        .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("refreshToken=refresh-token")))
        .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("HttpOnly")));


  }

}