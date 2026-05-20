package com.projectjy.domain.auth.dto.response;

public record LoginResponse(
    String accessToken,
    String refreshToken
) {

}
