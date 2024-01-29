package org.choongang.myPage.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.email.service.EmailVerifyService;
import org.choongang.member.MemberUtil;
import org.choongang.member.entities.Member;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.ValidationAnnotationUtils;

@Component
@RequiredArgsConstructor
public class ResignValidator implements Validator {

    private final MemberUtil memberUtil;
    private final PasswordEncoder encoder;

    private final EmailVerifyService emailVerifyService;


    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        RequestResign form = (RequestResign) target;

        String mode = form.getMode();

        if (mode.equals("step2")){
            validateStep2(form,errors);
        } else {
            validateStep1(form,errors);
        }

    }

    /**
     * 비밀번호, 비밀번호 확인 입력 여부
     * 비밀번호, 비밀번호 확인 일치 여부
     * 비밀번호, 회원정보 일치여부
     * @param form
     * @param errors
     */
    private void validateStep1(RequestResign form, Errors errors){

        String password = form.getPassword();
        String confirmPassword = form.getConfirmPassword();

        //입력여부
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"password" , "NotBlank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"confirmPassword" , "NotBlank");

        //동일 값인지
        if (StringUtils.hasText(password) && StringUtils.hasText((confirmPassword)) && !password.equals(confirmPassword)){

            errors.rejectValue("confirmPassword" , "Mismatch.password");
        }

        //로그인 회원 비밀번호와 같은지
        if (memberUtil.isLogin()){
            Member member = memberUtil.getMember();
            if(!encoder.matches(password , member.getPassword())){
                errors.rejectValue("password" , "Mismatch");
            }

        }


    }

    /**
     * 이메일 입력 여부
     * 이메일 인증 여부 확인
     * @param form
     * @param errors
     */
    private void validateStep2(RequestResign form, Errors errors){

        Integer authCode = form.getAuthCode();

        if(authCode == null){
            errors.rejectValue("authCode" , "NotNull");
        }

        if(authCode != null && !emailVerifyService.check(authCode)){
                errors.rejectValue("authCode" , "Mismatch");
        }

    }

}
