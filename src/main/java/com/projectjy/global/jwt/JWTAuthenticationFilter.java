package com.projectjy.global.jwt;

import com.projectjy.global.exception.CustomAuthEntryPoint;
import com.projectjy.global.exception.JwtTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

  private final JWTProvider jwtProvider;
  private final CustomAuthEntryPoint authenticationEntryPoint;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filter) throws ServletException, IOException {
    try{
      String token = request.getHeader("Authorization");

      if (token == null || !token.startsWith("Bearer ")){
        filter.doFilter(request, response);
        return;
      }

      token = token.split(" ")[1];

      if(!jwtProvider.getTokenType(token).equals("AccessToken")){
        filter.doFilter(request, response);
        return;
      }

      Long userId = jwtProvider.getUserId(token);
      String role = jwtProvider.getRole(token);

      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userId, null, List.of(new SimpleGrantedAuthority(role)));

      SecurityContextHolder.getContext().setAuthentication(auth);

      filter.doFilter(request, response);
    }catch (JwtTokenException e){
      authenticationEntryPoint.commence(request, response, e);
    }
  }
}
