package org.choongang.member.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.commons.ExceptionRestProcessor;
import org.choongang.commons.rests.JSONData;
import org.choongang.member.repositories.MemberRepository;
import org.choongang.member.service.follow.FollowService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class ApiMemberController implements ExceptionRestProcessor {

    private final MemberRepository memberRepository;
    private final FollowService followService;

    /**
     * 이메일 중복 여부 체크
     * @param email
     * @return
     */
    @GetMapping("/email_dup_check")
    public JSONData<Object> duplicateEmailCheck(@RequestParam("email") String email) {
        boolean isExists = memberRepository.existsByEmail(email);

        JSONData<Object> data = new JSONData<>();
        data.setSuccess(isExists);

        return data;
    }

    @GetMapping("/follow/{userId}")
    public JSONData<Object> follow(@PathVariable("userId") String userId) {
        followService.follow(userId);

        return new JSONData<>();
    }

    @GetMapping("/unfollow/{userId}")
    public JSONData<Object> unfollow(@PathVariable("userId") String userId) {
        followService.unfollow(userId);

        return new JSONData<>();
    }
}