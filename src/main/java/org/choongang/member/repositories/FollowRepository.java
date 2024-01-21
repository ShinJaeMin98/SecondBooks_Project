package org.choongang.member.repositories;

import jakarta.servlet.http.HttpServletRequest;
import org.choongang.commons.ListData;
import org.choongang.commons.Pagination;
import org.choongang.commons.RequestPaging;
import org.choongang.commons.Utils;
import org.choongang.member.entities.Follow;
import org.choongang.member.entities.Member;
import org.choongang.member.entities.QFollow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

import static org.springframework.data.domain.Sort.Order.desc;

public interface FollowRepository extends JpaRepository<Follow, Long>, QuerydslPredicateExecutor<Follow> {
    Follow findByFolloweeAndFollower(Member followee, Member follower);

    // member가 follow 하는 회원 수
    default long getTotalFollowings(Member member) {
        QFollow follow = QFollow.follow;

        return count(follow.followee.eq(member));
    }

    // member를 follow 하는 회원 수
    default long getTotalFollowers(Member member) {
        QFollow follow = QFollow.follow;

        return count(follow.follower.eq(member));
    }

    // member가 follow 하는 회원 목록
    default ListData<Member> getFollowings(Member member, RequestPaging paging, HttpServletRequest request) {

        int page = Utils.onlyPositiveNumber(paging.getPage(), 1);
        int limit = Utils.onlyPositiveNumber(paging.getLimit(), 20);

        QFollow follow = QFollow.follow;

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("createdAt")));

        Page<Follow> data = findAll(follow.followee.eq(member), pageable);
        List<Follow> follows = data.getContent();
        List<Member> items = null;
        if (follows != null) {
            items = follows.stream().map(Follow::getFollower).toList();
        }

        Pagination pagination = new Pagination(page, (int)data.getTotalElements(), 10, limit);

        return new ListData<>(items, pagination);
    }

    // member를 follow 하는 회원 목록
    default ListData<Member> getFollowers(Member member, RequestPaging paging, HttpServletRequest request) {

        int page = Utils.onlyPositiveNumber(paging.getPage(), 1);
        int limit = Utils.onlyPositiveNumber(paging.getLimit(), 20);

        QFollow follow = QFollow.follow;

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("createdAt")));

        Page<Follow> data = findAll(follow.follower.eq(member), pageable);
        List<Follow> follows = data.getContent();
        List<Member> items = null;
        if (follows != null) {
            items = follows.stream().map(Follow::getFollowee).toList();
        }

        Pagination pagination = new Pagination(page, (int)data.getTotalElements(), 10, limit, request);

        return new ListData<>(items, pagination);
    }

    // member가 follow 하는 회원 목록
    default List<Member> getFollowings(Member member) {
        QFollow follow = QFollow.follow;

        List<Follow> items = (List<Follow>)findAll(follow.followee.eq(member));

        if (items != null) {
            return items.stream().map(Follow::getFollower).toList();
        }

        return null;
    }

    // member를 follow 하는 회원 목록
    default List<Member> getFollowers(Member member) {
        QFollow follow = QFollow.follow;

        List<Follow> items = (List<Follow>)findAll(follow.follower.eq(member));

        if (items != null) {
            return items.stream().map(Follow::getFollowee).toList();
        }

        return null;
    }
}