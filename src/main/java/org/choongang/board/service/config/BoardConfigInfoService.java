package org.choongang.board.service.config;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.admin.board.controllers.BoardSearch;
import org.choongang.admin.board.controllers.RequestBoardConfig;
import org.choongang.board.entities.Board;
import org.choongang.board.entities.QBoard;
import org.choongang.board.repositories.BoardRepository;
import org.choongang.commons.ListData;
import org.choongang.commons.Pagination;
import org.choongang.commons.Utils;
import org.choongang.file.entities.FileInfo;
import org.choongang.file.service.FileInfoService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.springframework.data.domain.Sort.Order.desc;

@Service
@RequiredArgsConstructor
public class BoardConfigInfoService {
    private final BoardRepository boardRepository;
    private final FileInfoService fileInfoService;
    private final HttpServletRequest request;

    /**
     * 게시판 설정 조회
     *
     * @param bid
     * @return
     */
    public Board get(String bid){
        Board board = boardRepository.findById(bid).orElseThrow(BoardNotFoundException::new);

        addBoardInfo(board);

        return board;

    }

    public RequestBoardConfig getForm(String bid){
        Board board = get(bid);

        RequestBoardConfig form = new ModelMapper().map(board, RequestBoardConfig.class);
        form.setListAccessType(board.getListAccessType().name());
        form.setViewAccessType(board.getViewAccessType().name());
        form.setWriteAccessType(board.getWriteAccessType().name());
        form.setReplyAccessType(board.getReplyAccessType().name());
        form.setCommentAccessType(board.getCommentAccessType().name());

        form.setMode("edit");

        return form;
    }

    /**
     * 게시판 설정 추가 정보
     *  - 에디터 첨부 파일 목록
     *
     * @param board
     */
    public void addBoardInfo(Board board){
        String gid = board.getGid();

        List<FileInfo> htmlTopImages = fileInfoService.getListDone(gid, "html_top");
        List<FileInfo> htmlBottomImages = fileInfoService.getListDone(gid, "html_bottom");

        board.setHtmlTopImages(htmlTopImages);
        board.setHtmlBottomImages(htmlBottomImages);
    }

    /**
     * 게시판 설정 목록
     *
     * @param search
     * @return
     */
    public ListData<Board> getList(BoardSearch search){
        int page = Utils.onlyPositiveNumber(search.getPage(),1);
        int limit = Utils.onlyPositiveNumber(search.getLimit(), 20);

        QBoard board = QBoard.board;
        BooleanBuilder andBuilder = new BooleanBuilder();

        /*
            검색 조건 처리 Start
         */
        String bid = search.getBid();
        String bName = search.getBName();

        String sopt = search.getSopt();
        sopt = StringUtils.hasText(sopt) ? sopt.trim() : "ALL";
        String skey = search.getSkey(); // 키워드

        if(StringUtils.hasText(bid)){
            andBuilder.and(board.bid.contains(bid.trim()));
        }

        if(StringUtils.hasText(bName)){
            andBuilder.and(board.bName.contains(bName.trim()));
        }

        // 조건별 키워드 검색
        if (StringUtils.hasText(skey)){
            skey = skey.trim();

            BooleanExpression cond1 = board.bid.contains(skey);
            BooleanExpression cond2 = board.bName.contains(skey);


            if(sopt.equals("bid")){
                andBuilder.and(cond1);
            }else if(sopt.equals("bName")){
                andBuilder.and(cond2);
            }else {
                BooleanBuilder orBuilder = new BooleanBuilder();
                orBuilder.or(cond1)
                        .or(cond2);
                andBuilder.and(orBuilder);
        }
    }
    /*
        검색 조건 처리 END
     */

    Pageable pageable = PageRequest.of(page -1, limit, Sort.by(desc("createdAt")));
    Page<Board> data = boardRepository.findAll(andBuilder, pageable);

    Pagination pagination = new Pagination(page, (int)data.getTotalElements(),limit,10, request);

    return new ListData<>(data.getContent(), pagination);

    }
}
