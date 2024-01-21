package org.choongang.member.service.follow;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.board.controllers.BoardDataSearch;
import org.choongang.board.entities.BoardData;
import org.choongang.board.entities.QBoardData;
import org.choongang.board.repositories.BoardDataRepository;
import org.choongang.commons.ListData;
import org.choongang.commons.Pagination;
import org.choongang.commons.Utils;
import org.choongang.member.MemberUtil;
import org.choongang.member.entities.Member;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 팔로잉 회원 게시글 목록 조회
 * 팔로워 회원 게시글 목록 조회
 */
@Service
@RequiredArgsConstructor
public class FollowBoardService {

    private final MemberUtil memberUtil;
    private final FollowService followService;
    private final BoardDataRepository boardDataRepository;
    private final HttpServletRequest request;
    private final EntityManager em;

    /**
     *
     * @param mode
     *              follower : 로그인 회원을 팔로잉 하는 회원 게시글 목록
     *              following : 로그인 회원이 팔로잉한 회원 게시글 목록
     * @param search
     * @return
     */
    public ListData<BoardData> getList(String mode, BoardDataSearch search) {

        if (!memberUtil.isLogin()) { // 미로그인 상태 목록 조회 X
            return null;
        }

        int page = Utils.onlyPositiveNumber(search.getPage(), 1);
        int limit = Utils.onlyPositiveNumber(search.getLimit(), 20);
        int offset = (page - 1) * limit;

        mode = StringUtils.hasText(mode) ? mode : "follower";
        List<Member> members = mode.equals("following") ? followService.getFollowings() : followService.getFollowers();

        QBoardData boardData = QBoardData.boardData;
        BooleanBuilder andBuilder = new BooleanBuilder();
        andBuilder.and(boardData.member.in(members));

        /* 검색 조건 처리 S */

        String sopt = search.getSopt();
        String skey = search.getSkey();

        sopt = StringUtils.hasText(sopt) ? sopt.toUpperCase() : "ALL";

        if (StringUtils.hasText(skey)) {
            skey = skey.trim();

            BooleanExpression subjectCond = boardData.subject.contains(skey); // 제목 - subject LIKE '%skey%';
            BooleanExpression contentCond = boardData.content.contains(skey); // 내용 - content LIKE '%skey%';

            if (sopt.equals("SUBJECT")) { // 제목

                andBuilder.and(subjectCond);

            } else if (sopt.equals("CONTENT")) { // 내용

                andBuilder.and(contentCond);

            } else if (sopt.equals("SUBJECT_CONTENT")) { // 제목 + 내용

                BooleanBuilder orBuilder = new BooleanBuilder();
                orBuilder.or(subjectCond)
                        .or(contentCond);

                andBuilder.and(orBuilder);

            } else if (sopt.equals("POSTER")) { // 작성자 + 아이디 + 회원명
                BooleanBuilder orBuilder = new BooleanBuilder();
                orBuilder.or(boardData.poster.contains(skey))
                        .or(boardData.member.userId.contains(skey))
                        .or(boardData.member.name.contains(skey));

                andBuilder.and(orBuilder);
            }

        }

        // 특정 사용자로 게시글 한정 : 마이페이지에서 활용 가능
        String userId = search.getUserId();
        if (StringUtils.hasText(userId)) {
            andBuilder.and(boardData.member.userId.eq(userId));
        }

        // 게시글 분류 조회
        String category = search.getCategory();
        if (StringUtils.hasText(category)) {
            category = category.trim();
            andBuilder.and(boardData.category.eq(category));
        }

        /* 검색 조건 처리 E */

        PathBuilder<BoardData> pathBuilder = new PathBuilder<>(BoardData.class, "boardData");
        List<BoardData> items = new JPAQueryFactory(em).selectFrom(boardData)
                .leftJoin(boardData.member)
                .fetchJoin()
                .where(andBuilder)
                .offset(offset)
                .limit(limit)
                .orderBy(new OrderSpecifier(Order.DESC, pathBuilder.get("createdAt")))
                .fetch();

        int total = (int)boardDataRepository.count(andBuilder);
        Pagination pagination = new Pagination(page, total, 10, limit, request);

        return new ListData<>(items, pagination);
    }
}