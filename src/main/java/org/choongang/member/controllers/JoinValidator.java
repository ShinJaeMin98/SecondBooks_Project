package org.choongang.member.controllers;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.choongang.commons.validators.PasswordValidator;
import org.choongang.member.repositories.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class JoinValidator implements Validator, PasswordValidator {

    private final MemberRepository memberRepository;

    private final HttpSession session;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestJoin.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        /**
         * 1. 이메일, 아이디 중복 여부 체크
         * 2. 비밀번호 복잡성 체크 - 대소문자 1개 각각 포함, 숫자 1개 이상 포함, 특수문자도 1개 이상 포함
         * 3. 비밀번호, 비밀번호 확인 일치 여부 체크
         * 4. 이메일 인증 필수 여부 체크
         */

        RequestJoin form = (RequestJoin)target;
        String email = form.getEmail();
        String userId = form.getUserId();
        String password = form.getPassword();
        String confirmPassword = form.getConfirmPassword();

        // 1. 이메일, 아이디 중복 여부 체크
        if (StringUtils.hasText(email) && memberRepository.existsByEmail(email)) {
            errors.rejectValue("email", "Duplicated");
        }

        if (StringUtils.hasText(userId) && memberRepository.existsByUserId(userId)) {
            errors.rejectValue("userId", "Duplicated");
        }

        // 2. 비밀번호 복잡성 체크 - 대소문자 1개 각각 포함, 숫자 1개 이상 포함, 특수문자도 1개 이상 포함
        if (StringUtils.hasText(password) &&
                (!alphaCheck(password, true) || !numberCheck(password) || !specialCharsCheck(password))) {
            errors.rejectValue("password", "Complexity");
        }

        // 3. 비밀번호, 비밀번호 확인 일치 여부 체크
        if (StringUtils.hasText(password) && StringUtils.hasText(confirmPassword)
                && !password.equals(confirmPassword)) {
            errors.rejectValue("confirmPassword", "Mismatch.password");
        }

        // 4. 이메일 인증 필수 여부 체크
        boolean isVerified = (Boolean)session.getAttribute("EmailAuthVerified");
        if (!isVerified) { // 이메일 인증이 안된 경우
            errors.rejectValue("email", "Required.verified");
        }
    }
}
