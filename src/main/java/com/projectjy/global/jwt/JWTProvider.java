package com.projectjy.global.jwt;

import com.projectjy.global.exception.JwtTokenException;
import com.projectjy.global.exception.AuthErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JWTProvider {

  private final SecretKeySpec secretKeySpec;

  @Value("${jwt.access-expiration-ms}")
  private long accessExpirationMilliSeconds;

  public JWTProvider(@Value("${jwt.secret-key}") String secretKey) { //비밀 키 -> 바이트코드 배열로 변환
    this.secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SIG.HS256.key().build().getAlgorithm());
  }

  private Claims parseClaims(String token){ //Jwt 파싱하여 헤더, 페이로드 값 읽기 (위조 검증 추가)
    try{
      return Jwts
          .parser()
          .verifyWith(secretKeySpec)
          .build()
          .parseSignedClaims(token)
          .getPayload();
    }catch (ExpiredJwtException e) {
      throw new JwtTokenException(AuthErrorCode.EXPIRED_TOKEN);

    } catch (MalformedJwtException e) {
      throw new JwtTokenException(AuthErrorCode.MALFORMED_TOKEN);

    } catch (SignatureException | SecurityException e) {
      throw new JwtTokenException(AuthErrorCode.INVALID_SIGNATURE);

    } catch (UnsupportedJwtException e) {
      throw new JwtTokenException(AuthErrorCode.UNSUPPORTED_TOKEN);

    } catch (IllegalArgumentException e) {
      throw new JwtTokenException(AuthErrorCode.INVALID_TOKEN);
    }

  }

  public Long getUserId(String token){ //디코딩, Long
    return parseClaims(token).get("userId", Long.class);
  }

  public String getRole(String token){
    return parseClaims(token).get("role", String.class);
  }

  public String getTokenType(String token){ //Long
    return  parseClaims(token).get("type", String.class);
  }

  public String createAccessToken(Long userId){ //엑세스 토큰 생성
    return Jwts.builder()
        .claim("type", "AccessToken")
        .claim("userId", userId)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + accessExpirationMilliSeconds))
        .signWith(secretKeySpec)
        .compact();
  }
}
