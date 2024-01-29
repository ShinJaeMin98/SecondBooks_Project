package org.choongang.myPage.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestResign {    //탈퇴 시 입력받은 정보 커멘드 객체

    private String mode = "step1";

    //비번
    private String password;

    //비번확인
    private String confirmPassword;

    //이메일 인증 코드
    private Integer authCode;



}
