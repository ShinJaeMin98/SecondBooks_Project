package org.choongang.member.follow;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.choongang.commons.ListData;
import org.choongang.commons.RequestPaging;
import org.choongang.member.entities.Member;
import org.choongang.member.repositories.FollowRepository;
import org.choongang.member.repositories.MemberRepository;
import org.choongang.member.service.follow.FollowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@TestPropertySource(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@Transactional
@DisplayName("팔로우, 팔로잉 테스트")
public class FollowTest {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private FollowService followService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    private Member member1;
    private Member member2;
    private Member member3;

    private RequestPaging paging;

    @BeforeEach
    void init() {
        List<Member> members = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Member member = new Member();
            member.setUserId("user" + i);
            member.setGid("gid");
            member.setPassword("12345");
            member.setEmail("user" + i + "@test.org");
            member.setName("사용자" + i);
            members.add(member);
        }

        memberRepository.saveAllAndFlush(members);

        member1 = members.get(0);
        member2 = members.get(1);
        member3 = members.get(2);

        session.setAttribute("member", member1);

        followService.follow(member2);
        followService.follow(member3);

        paging = new RequestPaging();
    }

    /**
     * 테스트 시나리오1
     *
     * 1. member1은 member2, member3을 following 한다.
     *      member1에서 getFollowers 에서 member2와 member3이 나와야 하고
     *                  getTotalFollowers 에서는 2가 나와야 한다.
     */
    @Test
    @DisplayName("테스트 시나리오1")
    void test1() {

        List<Member> members = followRepository.getFollowings(member1);
        System.out.println(members);
        assertTrue(members.stream().map(Member::getUserId).anyMatch(u -> u.equals("user2") || u.equals("user3")));
        assertEquals(members.size(), followRepository.getTotalFollowings(member1));
    }

    /**
     * 테스트 시나리오2
     *
     * member2, member3는 각각 member1이라는 follower를 가지고 있어야 하고 getTotalFollowers()는 1명이 되어야 함
     */
    @Test
    @DisplayName("테스트 시나리오2")
    void test2() {
        ListData<Member> members1 = followRepository.getFollowers(member2, paging, request);
        ListData<Member> members2 = followRepository.getFollowers(member3, paging, request);

        assertEquals("user1", members1.getItems().get(0).getUserId());
        assertEquals("user1", members2.getItems().get(0).getUserId());
        assertEquals(1, followRepository.getTotalFollowers(member2));
        assertEquals(1, followRepository.getTotalFollowers(member3));
    }

    @Test
    @DisplayName("로그인 회원을 follow한 회원 목록 테스트 - followers")
    void test3() {
        ListData<Member> members = followService.getFollowers(paging);

        assertEquals(0, members.getItems().size());
    }

    @Test
    @DisplayName("로그인 회원이 follow한 회원 목록 테스트 - followings")
    void test4() {
        ListData<Member> members = followService.getFollowings(paging);
        assertEquals(2, members.getItems().size());
    }

}
