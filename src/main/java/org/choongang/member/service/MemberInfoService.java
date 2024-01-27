package org.choongang.member.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.choongang.commons.ListData;
import org.choongang.commons.Pagination;
import org.choongang.commons.Utils;
import org.choongang.file.entities.FileInfo;
import org.choongang.file.service.FileInfoService;
import org.choongang.member.controllers.MemberSearch;
import org.choongang.member.entities.Authorities;
import org.choongang.member.entities.Member;
import org.choongang.member.entities.QMember;
import org.choongang.member.repositories.MemberRepository;
import org.choongang.school.entities.School;
import org.choongang.school.service.SchoolInfoService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberInfoService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final FileInfoService fileInfoService;
    private final SchoolInfoService schoolInfoService;
    private final HttpServletRequest request;
    private final EntityManager em;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(username) // 이메일 조회
                .orElseGet(() -> memberRepository.findByUserId(username) // 아이디로 조회
                        .orElseThrow(() -> new UsernameNotFoundException(username)));

        List<SimpleGrantedAuthority> authorities = null;
        List<Authorities> tmp = member.getAuthorities();
        if (tmp != null) {
            authorities = tmp.stream()
                    .map(s -> new SimpleGrantedAuthority(s.getAuthority().name()))
                    .toList();
        }

        // 추가 정보 처리
        addMemberInfo(member);

        return MemberInfo.builder()
                .email(member.getEmail())
                .userId(member.getUserId())
                .password(member.getPassword())
                .member(member)
                .authorities(authorities)
                .enable(member.isEnable())
                .build();
    }

    /**
     * 회원 목록
     *
     * @param search
     * @return
     */
    public ListData<Member> getList(MemberSearch search) {

        int page = Utils.onlyPositiveNumber(search.getPage(), 1); // 페이지 번호
        int limit = Utils.onlyPositiveNumber(search.getLimit(), 20); // 1페이지당 레코드 갯수
        int offset = (page - 1) * limit; // 레코드 시작 위치 번호

        BooleanBuilder andBuilder = new BooleanBuilder();
        QMember member = QMember.member;

        /* 검색 조건 처리 S */

        String sopt = search.getSopt();
        sopt = StringUtils.hasText(sopt) ? sopt.trim() : "ALL";
        String skey = search.getSkey(); // 키워드

        // 조건별 키워드 검색
        if (StringUtils.hasText(skey)) {
            skey = skey.trim();

            BooleanExpression UserIdcond = member.userId.contains(skey);
            BooleanExpression Namecond = member.name.contains(skey);
            BooleanExpression SchoolNamecond = member.school.schoolName.contains(skey);
            //BooleanExpression authority = member.authority.contains(skey);

            if (sopt.equals("userId")) {
                andBuilder.and(UserIdcond);
            } else if (sopt.equals("name")) {
                andBuilder.and(Namecond);
            } else if (sopt.equals("schoolName")) {
                andBuilder.and(SchoolNamecond);
            } else { // 통합검색 : userId + name
                BooleanBuilder orBuilder = new BooleanBuilder();
                orBuilder.or(UserIdcond)
                        .or(Namecond)
                        .or(SchoolNamecond);
                andBuilder.and(orBuilder);
            }
        }

        /* 검색 조건 처리 E */

        PathBuilder<Member> pathBuilder = new PathBuilder<>(Member.class, "member");

        List<Member> items = new JPAQueryFactory(em)
                .selectFrom(member)
                .leftJoin(member.authorities)
                .fetchJoin()
                .where(andBuilder)
                .limit(limit)
                .offset(offset)
                .orderBy(new OrderSpecifier(Order.DESC, pathBuilder.get("createdAt")))
                .fetch();

        /* 페이징 처리 S */
        int total = (int)memberRepository.count(andBuilder); // 총 레코드 갯수

        Pagination pagination = new Pagination(page, total, 10, limit, request);
        /* 페이징 처리 E */

        return new ListData<>(items, pagination);
    }

    /**
     * 회원 추가 정보 처리
     *
     * @param member
     */
    public void addMemberInfo(Member member) {
        /* 프로필 이미지 처리 S */
        List<FileInfo> files = fileInfoService.getListDone(member.getGid());
        if (files != null && !files.isEmpty()) {
            member.setProfileImage(files.get(0));
        } else {
            School school = member.getSchool();
            schoolInfoService.addSchoolInfo(school);

            member.setProfileImage(school.getLogoImage());
        }
        /* 프로필 이미지 처리 E */
    }
}