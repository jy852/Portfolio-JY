package com.projectjy.domain.auth.serivce;

import com.projectjy.domain.auth.dto.request.LoginRequest;
import com.projectjy.domain.auth.dto.response.LoginResponse;
import com.projectjy.domain.auth.repository.AuthRepository;
import com.projectjy.domain.user.entity.User;
import com.projectjy.global.exception.BusinessException;
import com.projectjy.global.exception.ErrorCode;
import com.projectjy.global.jwt.JWTProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final AuthRepository authRepository;
  private final JWTProvider jwtProvider;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public LoginResponse login(LoginRequest request){

    User user = authRepository.findByUsername(request.username()).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

    validatePassword(request.password(), user.getPassword());

    String accessToken = jwtProvider.createAccessToken(user.getUserId(), user.getRole());
    String refreshToken = jwtProvider.createRefreshToken();

    return new LoginResponse(accessToken, refreshToken);
  }

  private void validatePassword(String inputPsw, String userPsw){

    if(!bCryptPasswordEncoder.matches(inputPsw, userPsw)){
      throw new BusinessException(ErrorCode.NOT_MATCHES_PASSWORD);
    }

  }

}
