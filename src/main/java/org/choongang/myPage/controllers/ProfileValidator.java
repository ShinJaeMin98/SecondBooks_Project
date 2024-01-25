package org.choongang.myPage.controllers;

import org.choongang.commons.validators.PasswordValidator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProfileValidator implements Validator, PasswordValidator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestProfile.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RequestProfile form = (RequestProfile) target;

        String password = form.getPassword();
        String confirmPassword = form.getConfirmPassword();

        /**
         * 비밀번호 변경은 사용자가 입력한 경우만 체크
         */
        if (StringUtils.hasText(password)) {
            if(!alphaCheck(password, false) || !numberCheck(password) || !specialCharsCheck(password)) {
                errors.rejectValue("password","Complexity");
            }

            if(!StringUtils.hasText(confirmPassword)) {
                errors.rejectValue("confirmPassword", "NotBlank");
            }

            if (!password.equals(confirmPassword)) {
                errors.rejectValue("confirmPassword","Mismatch.password");
            }
        }
    }
}
