package org.choongang.board.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.choongang.board.controllers.BoardDataSearch;
import org.choongang.board.controllers.RequestBoard;
import org.choongang.board.entities.*;
import org.choongang.board.repositories.BoardDataRepository;
import org.choongang.board.repositories.BoardRepository;
import org.choongang.board.repositories.BoardViewRepository;
import org.choongang.board.service.comment.CommentInfoService;
import org.choongang.board.service.config.BoardConfigInfoService;
import org.choongang.commons.ListData;
import org.choongang.commons.Pagination;
import org.choongang.commons.Utils;
import org.choongang.file.entities.FileInfo;
import org.choongang.file.service.FileInfoService;
import org.choongang.member.MemberUtil;
import org.choongang.member.entities.Member;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardInfoService {

    private final EntityManager em;
    private final BoardRepository boardRepository;
    private final BoardDataRepository boardDataRepository;
    private final BoardViewRepository boardViewRepository;

    private final BoardConfigInfoService configInfoService;

    private final FileInfoService fileInfoService;
    private final HttpServletRequest request;

    private final MemberUtil memberUtil;
    private final Utils utils;

    private final CommentInfoService commentInfoService;



    /**
     * 게시글 조회
     *
     * @param seq : 게시글 번호
     * @return
     */
    public BoardData get(Long seq) {
        BoardData boardData = boardDataRepository.findById(seq).orElseThrow(BoardDataNotFoundException::new);

        if(memberUtil.isLogin() && boardData.getBoard().isSchoolOnly()) { // 로그인 회원의 학교 게시물
            Member member = memberUtil.getMember();
            if (!member.getSchool().getDomain().equals(boardData.getText2())) {
                throw new BoardDataNotFoundException();
            }
        }
        addBoardData(boardData);

        List<CommentData> comments = commentInfoService.getList(seq);
        boardData.setComments(comments);

        return boardData;
    }

    /**
     * BoardData -> RequestBoard
     *
     * @param data : 게시글 데이터(BoardData), 게시글 번호(Long)
     * @return
     */
    public RequestBoard getForm(Object data) {
        BoardData boardData = null;
        if(data instanceof BoardData) {
            boardData = (BoardData) data;
        } else {
            Long seq = (Long) data;
            boardData = get(seq);
        }

        RequestBoard form = new ModelMapper().map(boardData, RequestBoard.class);
        form.setMode("update");
        form.setBid(boardData.getBoard().getBid());
        return form;
    }


    /**
     * 특정 게시판 목록을 조회
     * 
     * @param bid : 게시판 ID
     * @param search
     * @return
     */
    public ListData<BoardData> getList(String bid, BoardDataSearch search) {

        Board board = StringUtils.hasText(bid) ? configInfoService.get(bid) : new Board();
        
        int page = Utils.onlyPositiveNumber(search.getPage(), 1);
        
        int limit = Utils.onlyPositiveNumber(search.getLimit(), board.getRowsPerPage());
        int offset = (page - 1) * limit; // 레코드 시작 위치
        
        QBoardData boardData = QBoardData.boardData;
        BooleanBuilder andBuilder = new BooleanBuilder();

        if (StringUtils.hasText(bid)) {
            andBuilder.and(boardData.board.bid.eq(bid)); // 게시판 ID
        }

        List<String> bids = search.getBid();
        if(bids != null && !bids.isEmpty()){
            andBuilder.and(boardData.board.bid.in(bids));
        }

        if(memberUtil.isLogin() && board.isSchoolOnly()){ // 로그인 회원의 학교 게시물만 조회

           String domain = memberUtil.getMember().getSchool().getDomain();
           andBuilder.and(boardData.text2.eq(domain));
        }


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

            } else if (sopt.equals("POSTER")) { // 작성자 + ID + 회원명

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

        List<BoardData> items = new JPAQueryFactory(em)
                .selectFrom(boardData)
                .leftJoin(boardData.member)
                .fetchJoin()
                .offset(offset)
                .limit(limit)
                .where(andBuilder)
                .orderBy(
                        new OrderSpecifier(Order.DESC, pathBuilder.get("notice")),
                        new OrderSpecifier(Order.DESC, pathBuilder.get("createdAt"))
                )
                .fetch();

        // 게시글 전체 개수
        long total = boardDataRepository.count(andBuilder);
        int ranges = utils.isMobile() ? board.getPageCountMobile() : board.getPageCountPc();
        Pagination pagination = new Pagination(page, (int)total, ranges, limit, request);

        items.forEach(this::addBoardData);

        return new ListData<>(items, pagination);
    }

    public ListData<BoardData> getList(BoardDataSearch search) {
        return getList(null, search);
    }

    /**
     * 최신 게시글
     *
     * @param bid : 게시판 아이디
     * @param limit : 조회할 갯수
     * @return
     */
    public List<BoardData> getLatest(String bid, int limit) {

        BoardDataSearch search = new BoardDataSearch();
        search.setLimit(limit);

        ListData<BoardData> data = getList(bid, search);

        return data.getItems();
    }

    public List<BoardData> getLatest(String bid) {
        return getLatest(bid, 10);
    }


    /**
     * 게시글 추가 정보 처리
     *
     * @param boardData
     */
    public void addBoardData(BoardData boardData){
        /* 파일 정보 추가 S */
        String gid = boardData.getGid();

        List<FileInfo> editorFiles = fileInfoService.getListDone(gid, "editor");
        List<FileInfo> attachFiles = fileInfoService.getListDone(gid, "attach");

        boardData.setEditorFiles(editorFiles);
        boardData.setAttachFiles(attachFiles);
        /* 파일 정보 추가 E */

        /* 수정, 삭제 권한 정보 처리 S */
        boolean editable = false, deletable = false, mine = false;
        Member _member = boardData.getMember(); // null - 비회원, X null -> 회원

        // 관리자 -> 삭제, 수정 모두 가능
        if (memberUtil.isAdmin()) {
            editable = true;
            deletable = true;
        }

        // 회원 -> 직접 작성한 게시글만 삭제, 수정 가능
        Member member = memberUtil.getMember();
        if (_member != null && memberUtil.isLogin() && _member.getUserId().equals(member.getUserId())) {
            editable = true;
            deletable = true;
            mine = true;
        }

        // 비회원 -> 비회원 비밀번호가 확인 된 경우 삭제, 수정 가능
        // 비회원 비밀번호 인증 여부 세션에 있는 guest_confirmed_게시글번호 true -> 인증
        HttpSession session = request.getSession();
        String key = "guest_confirmed_" + boardData.getSeq();
        Boolean guestConfirmed = (Boolean)session.getAttribute(key);
        if (_member == null && guestConfirmed != null && guestConfirmed) {
            editable = true;
            deletable = true;
            mine = true;
        }

        boardData.setEditable(editable);
        boardData.setDeletable(deletable);
        boardData.setMine(mine);

        // 수정 버튼 노출 여부
        // 관리자 - 노출, 회원 게시글 - 직접 작성한 게시글, 비회원
        boolean showEditButton = memberUtil.isAdmin() || mine || _member == null;
        boolean showDeleteButton = showEditButton;

        boardData.setShowEditButton(showEditButton);
        boardData.setShowDeleteButton(showDeleteButton);

        /* 수정, 삭제 권한 정보 처리 E */

    }

    /**
     * 게시글 조회수 업데이트
     * 
     * @param seq : 게시글 번호
     */
    public void updateViewCount(Long seq) {

        BoardData data = boardDataRepository.findById(seq).orElse(null);
        if(data == null) return;

        try {
            int uid = memberUtil.isLogin() ? memberUtil.getMember().getSeq().intValue() : utils.guestUid();

            BoardView boardView = new BoardView(seq, uid);

            boardViewRepository.saveAndFlush(boardView);
        } catch (Exception e) {}

        // 조회수 카운팅 -> 게시글에 업데이트
        QBoardView bv = QBoardView.boardView;
        int viewCount = (int)boardViewRepository.count(bv.seq.eq(seq));

        data.setViewCount(viewCount);

        boardViewRepository.flush();
    }
}
