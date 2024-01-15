package org.choongang.board.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.commons.validators.PasswordValidator;
import org.choongang.member.MemberUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class BoardFormValidator implements Validator, PasswordValidator {

    private final MemberUtil memberUtil;
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestBoard.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RequestBoard form = (RequestBoard) target;

        if (!memberUtil.isLogin()) {
            String guestPw = form.getGuestPw();

            /**
             * 비회원
             *  1. 필수 여부
             *  2. 6자리 이상
             *  3. 알파벳 + 숫자
             *
             */

            if (!StringUtils.hasText(guestPw)) {
                errors.rejectValue("guestPw", "NotBlank");
            }

            if (StringUtils.hasText(guestPw) && guestPw.length() < 6) {
                errors.rejectValue("guestPw", "Size");
            }

            if (StringUtils.hasText(guestPw) && (!alphaCheck(guestPw, true) || !numberCheck(guestPw))) {
                errors.rejectValue("guestPw", "Complexity");
            }
        }

    }
}
