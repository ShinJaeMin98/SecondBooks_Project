package org.choongang.member.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.choongang.file.service.FileUploadService;
import org.choongang.member.MemberUtil;
import org.choongang.member.entities.Member;
import org.choongang.member.repositories.MemberRepository;
import org.choongang.myPage.controllers.RequestProfile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class MemberUpdateService {
    private final MemberRepository memberRepository;
    private final FileUploadService fileUploadService;
    private final HttpSession session;
    private final MemberUtil memberUtil;

    private final PasswordEncoder encoder;


    public void update(RequestProfile form) {
        Member member = memberUtil.getMember();
        member.setName(form.getName());
        String password = form.getPassword();
        if(StringUtils.hasText(password)) {
            member.setPassword(encoder.encode(password.trim()));
        }

        member.setProfileImage(form.getProfileImage());

        memberRepository.saveAndFlush(member);

        fileUploadService.processDone(member.getGid());

        session.setAttribute("member", member);
    }
}
