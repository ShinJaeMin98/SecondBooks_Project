package org.choongang.board.controllers.comment;


import lombok.RequiredArgsConstructor;
import org.choongang.board.controllers.comment.RequestComment;
import org.choongang.commons.validators.PasswordValidator;
import org.choongang.member.MemberUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class CommentFormValidator implements Validator, PasswordValidator {

    private final MemberUtil memberUtil;


    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestComment.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if(memberUtil.isLogin()){   //로그인 회원일 경우 비밀번호 검증 절차 생략
            return;
        }

        RequestComment form = (RequestComment) target;
        String guestPw = form.getGuestPw();
        if(!StringUtils.hasText(guestPw)){
            errors.rejectValue("guestPw" , "NotBlank");
        }
        if(StringUtils.hasText(guestPw)){
            if(guestPw.length() < 6){
                errors.rejectValue("guestPw" , "Size");
            }
            if(!alphaCheck(guestPw, true) || !numberCheck(guestPw)){
                errors.rejectValue("guestPw" , "Complexity");
            }


        }
    }
}
