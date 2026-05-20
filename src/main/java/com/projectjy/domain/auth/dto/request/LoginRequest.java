package com.projectjy.domain.auth.dto.request;


import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record LoginRequest(

    @Length(min = 1, max=100, message = "아이디는 최대 100글자만 입력할 수 있습니다.")
    @NotBlank(message = "아이디를 입력해주세요.")
    String username,

    @Length(min = 1, max=100, message = "비밀번호는 최대 100글자만 입력할 수 있습니다.")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    String password

) {

}
