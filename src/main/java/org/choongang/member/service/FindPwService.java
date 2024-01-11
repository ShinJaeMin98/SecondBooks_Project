package org.choongang.member.service;


import lombok.RequiredArgsConstructor;
import org.choongang.commons.Utils;
import org.choongang.email.service.EmailMessage;
import org.choongang.email.service.EmailSendService;
import org.choongang.member.controllers.FindPwValidator;
import org.choongang.member.controllers.RequestFindPw;
import org.choongang.member.entities.Member;
import org.choongang.member.repositories.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.HashMap;
import java.util.Map;

/**
 * 비밀번호 찾기 양식 검증 및 초기화 메일 전송
 *
 */
@Service
@RequiredArgsConstructor
public class FindPwService {

    private final FindPwValidator validator;
    private final MemberRepository repository;
    private final EmailSendService sendService;
    private final PasswordEncoder encoder;
    private final Utils utils;

    public void process(RequestFindPw form, Errors errors) {
        validator.validate(form, errors);
        if (errors.hasErrors()) { // 유효성 검사 실패시에는 처리 중단
            return;
        }

        // 비밀번호 초기화
        reset(form.email());

    }

    public void reset(String email) {
        /* 비밀번호 초기화 S */
        Member member = repository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        String newPassword = utils.randomChars(12); // 초기화 비밀번호는 12자로 생성
        member.setPassword(encoder.encode(newPassword));

        repository.saveAndFlush(member);

        /* 비밀번호 초기화 E */
        EmailMessage emailMessage = new EmailMessage(email, Utils.getMessage("Email.password.reset", "commons"), Utils.getMessage("Email.password.reset", "commons"));
        Map<String, Object> tplData = new HashMap<>();
        tplData.put("password", newPassword);
        sendService.sendMail(emailMessage, "password_reset", tplData);
    }
}