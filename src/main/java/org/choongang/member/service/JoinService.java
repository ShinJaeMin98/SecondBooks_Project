package org.choongang.member.service;

import lombok.RequiredArgsConstructor;
import org.choongang.school.service.SchoolInfoService;
import org.choongang.file.service.FileUploadService;
import org.choongang.member.Authority;
import org.choongang.member.controllers.JoinValidator;
import org.choongang.member.controllers.RequestJoin;
import org.choongang.member.entities.Authorities;
import org.choongang.member.entities.Member;
import org.choongang.member.repositories.AuthoritiesRepository;
import org.choongang.member.repositories.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

@Service
@RequiredArgsConstructor
@Transactional
public class JoinService {

    private final MemberRepository memberRepository;
    private final AuthoritiesRepository authoritiesRepository;
    private final JoinValidator validator;
    private final PasswordEncoder encoder;
    private final FileUploadService uploadService;
    private final SchoolInfoService schoolSearchService;

    public void process(RequestJoin form, Errors errors) {
        validator.validate(form, errors);
        if (errors.hasErrors()) {
            return;
        }

        // 비밀번호 BCrypt로 해시화
        String hash = encoder.encode(form.getPassword());

        //Member member = new ModelMapper().map(form, Member.class);
        Member member = new Member();
        member.setEmail(form.getEmail());
        member.setSchool(schoolSearchService.findSchoolByDomain(form.getDomain()));
        member.setName(form.getName());
        member.setPassword(hash);
        member.setUserId(form.getUserId());
        member.setGid(form.getGid());

        process(member);

        // 회원 가입시에는 일반 사용자 권한 부여(USER)
        Authorities authorities = new Authorities();
        authorities.setMember(member);
        authorities.setAuthority(Authority.USER);
        authoritiesRepository.saveAndFlush(authorities);

        // 파일 업로드 완료 처리
        uploadService.processDone(form.getGid());

    }

    public void process(Member member) {
        memberRepository.saveAndFlush(member);
    }
}