package com.projectjy.domain.auth.serivce;


import com.projectjy.domain.auth.dto.request.LoginRequest;
import com.projectjy.domain.auth.dto.response.LoginResponse;
import com.projectjy.domain.auth.repository.AuthRepository;
import com.projectjy.domain.user.entity.User;
import com.projectjy.domain.user.entity.UserRole;
import com.projectjy.global.exception.BusinessException;
import com.projectjy.global.exception.ErrorCode;
import com.projectjy.global.jwt.JWTProvider;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Mock
  private AuthRepository authRepository;

  @Mock
  private JWTProvider jwtProvider;

  @InjectMocks
  private AuthService authService;

  @Test
  @DisplayName("아이디가 불일치할 경우 에러 반환")
  void invalid_id_throw_exceptions(){

    //given
    String username = "testid";
    String password = "testpassword";

    LoginRequest request = new LoginRequest(username, password);

    Mockito.when(authRepository.findByUsername(request.username())).thenReturn(Optional.empty());
    //when

    //then
    Throwable thrown = Assertions.catchThrowable(() -> authService.login(request));
    BusinessException exception = (BusinessException) thrown;

    Assertions.assertThat(thrown).isInstanceOf(BusinessException.class);
    Assertions.assertThat(exception.getError()).isEqualTo(ErrorCode.USER_NOT_FOUND);

  }

  @Test
  @DisplayName("비밀번호가 불일치할 경우 에러 반환")
  void invalid_password_throw_exceptions(){

    //given
    String username = "testid";
    String password = "testpassword";

    LoginRequest request = new LoginRequest(username, password);

    String encodedPassword = "encodedPassword";

    User user = User.builder()
        .userId(1L)
        .username(username)
        .password(encodedPassword)
        .build();

    Mockito.when(authRepository.findByUsername(request.username())).thenReturn(Optional.ofNullable(user));
    Mockito.when(bCryptPasswordEncoder.matches(request.password(), encodedPassword)).thenReturn(false);

    //when
    Throwable thrown = Assertions.catchThrowable(() -> authService.login(request));
    BusinessException exception = (BusinessException) thrown;

    //then
    Assertions.assertThat(thrown).isInstanceOf(BusinessException.class);
    Assertions.assertThat(exception.getError()).isEqualTo(ErrorCode.NOT_MATCHES_PASSWORD);
  }

  @Test
  @DisplayName("아이디 및 비밀번호가 일치할 경우 엑세스 및 리프레시 토큰 반환")
  void corrected_id_password_then_generate_access_token(){
    //given
    String username = "username";
    String password = "password";

    LoginRequest request = new LoginRequest(username, password);

    String encodedPassword = "encodedPassword";

    User user = User.builder()
        .userId(1L)
        .username(username)
        .password(encodedPassword)
        .role(UserRole.ROLE_ADMIN)
        .build();

    Mockito.when(authRepository.findByUsername(request.username())).thenReturn(Optional.ofNullable(user));
    Mockito.when(bCryptPasswordEncoder.matches(request.password(), encodedPassword)).thenReturn(true);
    Mockito.when(jwtProvider.createAccessToken(user.getUserId(), user.getRole())).thenReturn("access-token");
    Mockito.when(jwtProvider.createRefreshToken()).thenReturn("refresh-token");

    //when
    LoginResponse token = authService.login(request);

    //then
    Assertions.assertThat(token.accessToken()).isEqualTo("access-token");
    Assertions.assertThat(token.refreshToken()).isEqualTo("refresh-token");
  }

}