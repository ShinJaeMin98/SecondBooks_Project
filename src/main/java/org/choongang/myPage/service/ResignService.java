package org.choongang.myPage.service;

import jakarta.persistence.Transient;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.choongang.file.service.FileDeleteService;
import org.choongang.member.MemberUtil;
import org.choongang.member.entities.Member;
import org.choongang.member.repositories.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ResignService {

    private final MemberRepository repository;
    private final HttpSession session;
    private final MemberUtil memberUtil;
    private final FileDeleteService fileDeleteService;

    public void resign(){
        Member member = memberUtil.getMember();
        member.setEnable(false);
        //회원 개인정보 삭제
        member.setName("****");

        String gid = member.getGid();

        //프로필 이미지 삭제
        fileDeleteService.delete(gid);
        repository.saveAndFlush(member);

        session.invalidate();

    }







}
