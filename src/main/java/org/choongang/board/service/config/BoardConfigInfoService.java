package org.choongang.board.service.config;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.admin.board.controllers.BoardSearch;
import org.choongang.admin.board.controllers.RequestBoardConfig;
import org.choongang.board.entities.Board;
import org.choongang.board.entities.QBoard;
import org.choongang.board.repositories.BoardDataRepository;
import org.choongang.board.repositories.BoardRepository;
import org.choongang.commons.ListData;
import org.choongang.commons.Pagination;
import org.choongang.commons.Utils;
import org.choongang.file.entities.FileInfo;
import org.choongang.file.service.FileInfoService;
import org.choongang.member.MemberUtil;
import org.choongang.member.entities.Member;
import org.choongang.school.entities.School;
import org.choongang.school.service.SchoolInfoService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

import static org.springframework.data.domain.Sort.Order.desc;

@Service
@RequiredArgsConstructor
public class BoardConfigInfoService {
    private final BoardRepository boardRepository;
    private final BoardDataRepository boardDataRepository;
    private final FileInfoService fileInfoService;
    private final HttpServletRequest request;

    private final SchoolInfoService schoolInfoService;
    private final MemberUtil memberUtil;

    /**
     * 게시판 설정 조회
     *
     * @param bid
     * @return
     */
    public Board get(String bid) {
        Board board = boardRepository.findById(bid).orElseThrow(BoardNotFoundException::new);

        addBoardInfo(board);

        return board;

    }

    public RequestBoardConfig getForm(String bid) {
        Board board = get(bid);

        RequestBoardConfig form = new ModelMapper().map(board, RequestBoardConfig.class);
        form.setListAccessType(board.getListAccessType().name());
        form.setViewAccessType(board.getViewAccessType().name());
        form.setWriteAccessType(board.getWriteAccessType().name());
        form.setReplyAccessType(board.getReplyAccessType().name());
        form.setCommentAccessType(board.getCommentAccessType().name());
        form.setMenuLocation(board.getMenuLocation().name());
        form.setMode("edit");

        return form;
    }

    /**
     * 게시판 설정 추가 정보
     *      - 에디터 첨부 파일 목록
     * @param board
     */
    public void addBoardInfo(Board board) {
        String gid = board.getGid();

        List<FileInfo> htmlTopImages = fileInfoService.getListDone(gid, "html_top");

        List<FileInfo> htmlBottomImages = fileInfoService.getListDone(gid, "html_bottom");

        if (board.isSchoolOnly()) { // 학교 전용 게시판
            if (memberUtil.isLogin()) {
                Member member = memberUtil.getMember();
                School school = member.getSchool();
                schoolInfoService.addSchoolInfo(school);

                board.setHtmlTopImages(Arrays.asList(school.getBanner_top()));
                board.setHtmlBottomImages(Arrays.asList(school.getBanner_bottom()));
                board.setHtmlBottom("");
                board.setHtmlTop("");
            }


        } else { // 일반 게시판
            board.setHtmlTopImages(htmlTopImages);
            board.setHtmlBottomImages(htmlBottomImages);
        }

        List<FileInfo> logo1 = fileInfoService.getListDone(gid, "logo1");
        List<FileInfo> logo2 = fileInfoService.getListDone(gid, "logo2");
        List<FileInfo> logo3 = fileInfoService.getListDone(gid, "logo3");

        if (logo1 != null && !logo1.isEmpty()) {
            board.setLogo1(logo1.get(0));
        }

        if (logo2 != null && !logo2.isEmpty()) {
            board.setLogo2(logo2.get(0));
        }

        if (logo3 != null && !logo3.isEmpty()) {
            board.setLogo3(logo3.get(0));
        }
    }

    /**
     * 게시판 설정 목록
     *
     * @param search
     * @return
     */
    public ListData<Board> getList(BoardSearch search, boolean isAll) {
        int page = Utils.onlyPositiveNumber(search.getPage(), 1);
        int limit = Utils.onlyPositiveNumber(search.getLimit(), 20);

        QBoard board = QBoard.board;
        BooleanBuilder andBuilder = new BooleanBuilder();

        /* 검색 조건 처리 S */
        String bid = search.getBid();
        List<String> bids = search.getBids();
        String bName = search.getBName();

        String sopt = search.getSopt();
        sopt = StringUtils.hasText(sopt) ? sopt.trim() : "ALL";
        String skey = search.getSkey(); // 키워드

        if (StringUtils.hasText(bid)) { // 게시판 ID
            andBuilder.and(board.bid.contains(bid.trim()));
        }

        // 게시판 ID 여러개 조회
        if (bids != null && !bids.isEmpty()) {
            andBuilder.and(board.bid.in(bids));
        }

        if (!isAll) {   // 노출 상태인 게시판만 조회
            andBuilder.and(board.active.eq(true));
        }

        if (StringUtils.hasText(bName)) { // 게시판 명
            andBuilder.and(board.bName.contains(bName.trim()));
        }

        // 조건별 키워드 검색
        if (StringUtils.hasText(skey)) {
            skey = skey.trim();

            BooleanExpression cond1 = board.bid.contains(skey);
            BooleanExpression cond2 = board.bName.contains(skey);

            if (sopt.equals("bid")) {
                andBuilder.and(cond1);
            } else if (sopt.equals("bName")) {
                andBuilder.and(cond2);
            } else { // 통합검색 : bid + bName
                BooleanBuilder orBuilder = new BooleanBuilder();
                orBuilder.or(cond1)
                        .or(cond2);
                andBuilder.and(orBuilder);
            }
        }

        /* 검색 조건 처리 E */

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("createdAt")));
        Page<Board> data = boardRepository.findAll(andBuilder, pageable);

        Pagination pagination = new Pagination(page, (int)data.getTotalElements(), limit, 10, request);

        return new ListData<>(data.getContent(), pagination);
    }

    /**
     * 노출 상태인 게시판 목록
     *
     * @param search
     * @return
     */
    public ListData<Board> getList(BoardSearch search) {
        return getList(search, false);
    }

    /**
     *  노출 가능한 모든 게시판 목록
     *
     * @return
     */
    public List<Board> getList() {
        QBoard board = QBoard.board;

        List<Board> items = (List<Board>) boardRepository.findAll(board.active.eq(true), Sort.by(desc("listOrder"), desc("createdAt")));

        return items;
    }

    /**
     *
     * @param userId
     * @return
     */
    public List<Board> getUserBoardsInfo(String userId) {
        List<String> bids = boardDataRepository.getUserBoards(userId);

        QBoard board = QBoard.board;
        List<Board> items = (List<Board>)boardRepository.findAll(board.bid.in(bids), Sort.by(desc("createdAt")));

        return items;
    }
}