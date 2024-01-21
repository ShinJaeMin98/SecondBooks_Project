package org.choongang.member.service.follow;

import com.querydsl.core.BooleanBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.commons.ListData;
import org.choongang.commons.RequestPaging;
import org.choongang.member.MemberUtil;
import org.choongang.member.entities.Follow;
import org.choongang.member.entities.Member;
import org.choongang.member.entities.QFollow;
import org.choongang.member.repositories.FollowRepository;
import org.choongang.member.repositories.MemberRepository;
import org.choongang.member.service.MemberInfoService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;
    private final MemberInfoService memberInfoService;
    private final MemberUtil memberUtil;
    private final HttpServletRequest request;


    /**
     * 팔로잉
     *
     * @param follower : 팔로잉할 회원
     */
    public void follow(Member follower) {
        // 팔로잉 기능은 회원 전용 기능이므로 로그인상태가 아니라면 처리 안함
        if (!memberUtil.isLogin()) {
            return;
        }

        try {
            Member followee = memberUtil.getMember();

            // 팔로워과 팔로잉 하는 사용자가 같을 수 없으므로 체크
            if (follower.getUserId().equals(followee.getUserId())) {
                return;
            }

            Follow follow = Follow.builder()
                    .followee(followee)
                    .follower(follower)
                    .build();

            followRepository.saveAndFlush(follow);
        } catch (Exception e) {e.printStackTrace();} // 동일한 follow 데이터가 있으면 Unique 제약 조건 예외가 발생하므로 무시
    }

    public void follow(Long seq) {
        Member follower = memberRepository.findById(seq).orElse(null);
        if (follower == null) {
            return;
        }

        follow(follower);
    }

    public void follow(String userId) {
        Member follower = memberRepository.findByUserId(userId).orElse(null);

        if (follower == null) {
            return;
        }

        follow(follower);
    }

    /**
     * 언팔로잉
     *
     * @param follower : 팔로잉을 취소할 회원
     */
    public void unfollow(Member follower) {
        // 언팔로잉 기능은 회원 전용 기능이므로 로그인상태가 아니라면 처리 안함
        if (!memberUtil.isLogin()) {
            return;
        }

        if (follower == null) {
            return;
        }

        Member followee = memberUtil.getMember();

        Follow follow = followRepository.findByFolloweeAndFollower(followee, follower);
        followRepository.delete(follow);
        followRepository.flush();
    }

    public void unfollow(Long seq) {
        Member follower = memberRepository.findById(seq).orElse(null);
        if (follower == null) {
            return;
        }

        unfollow(follower);
    }

    public void unfollow(String userId) {
        Member follower = memberRepository.findByUserId(userId).orElse(null);
        if (follower == null) {
            return;
        }

        unfollow(follower);
    }

    /**
     * 로그인 회원을 follow 한 회원 목록
     * @return
     */
    public ListData<Member> getFollowers(RequestPaging paging) {
        if (!memberUtil.isLogin()) {
            return null;
        }

        return followRepository.getFollowers(memberUtil.getMember(), paging, request);
    }

    /**
     * 로그인 회원이 follow한 회원목록
     *
     * @return
     */
    public ListData<Member> getFollowings(RequestPaging paging) {
        if (!memberUtil.isLogin()) {
            return null;
        }

        return followRepository.getFollowings(memberUtil.getMember(), paging, request);
    }

    /**
     * 로그인 회원을 follow 한 회원 목록
     * @return
     */
    public List<Member> getFollowers() {
        if (!memberUtil.isLogin()) {
            return null;
        }

        return followRepository.getFollowers(memberUtil.getMember());
    }

    /**
     * 로그인 회원이 follow한 회원목록
     *
     * @return
     */
    public List<Member> getFollowings() {
        if (!memberUtil.isLogin()) {
            return null;
        }

        return followRepository.getFollowings(memberUtil.getMember());
    }


    public long getTotalFollowers() {

        if (memberUtil.isLogin()) {
            return followRepository.getTotalFollowers(memberUtil.getMember());
        }

        return 0L;
    }

    public long getTotalFollowings() {

        if (memberUtil.isLogin()) {
            return followRepository.getTotalFollowings(memberUtil.getMember());
        }

        return 0L;
    }

    /**
     * 팔로우, 팔로잉 목록
     * @param mode : follower - 팔로워 회원 목록, following : 팔로잉 회원 목록
     * @param paging
     * @return
     */
    public ListData<Member> getList(String mode, RequestPaging paging) {
        mode = StringUtils.hasText(mode) ? mode : "follower";

        ListData<Member> data = mode.equals("following") ? getFollowings(paging) : getFollowers(paging);
        data.getItems().forEach(memberInfoService::addMemberInfo);

        return data;
    }

    /**
     * 팔로잉 상태인지 체크
     *
     * @param userId
     * @return
     */
    public boolean followed(String userId) {
        if (!memberUtil.isLogin()) {
            return false;
        }

        QFollow follow = QFollow.follow;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(follow.follower.userId.eq(userId))
                .and(follow.followee.in(memberUtil.getMember()));

        return followRepository.exists(builder);
    }
}