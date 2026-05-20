package com.projectjy.global.config.security;

import com.projectjy.global.exception.CustomAccessDeniedHandler;
import com.projectjy.global.exception.CustomAuthEntryPoint;
import com.projectjy.global.jwt.JWTAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {


  private final JWTAuthenticationFilter jwtAuthenticationFilter;
  private final CustomAuthEntryPoint customAuthEntryPoint;
  private final CustomAccessDeniedHandler customAccessDeniedHandler;

  @Bean
  public RoleHierarchy roleHierarchy(){
    return RoleHierarchyImpl
        .withDefaultRolePrefix()
        .role("ADMIN").implies("USER")
        .build();
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder(){
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

    http
        .formLogin(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(auth -> auth.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(exception -> exception
            .authenticationEntryPoint(customAuthEntryPoint)
            .accessDeniedHandler(customAccessDeniedHandler)
        )
        .authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/auth/login").permitAll())
        .authorizeHttpRequests(auth -> auth
            .anyRequest().authenticated()
        )
        .cors(cors -> {});

    return http.build();
  }

}
