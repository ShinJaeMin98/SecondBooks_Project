package org.choongang.board.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.board.entities.Board;
import org.choongang.board.entities.BoardData;
import org.choongang.board.repositories.BoardDataRepository;
import org.choongang.board.service.config.BoardConfigInfoService;
import org.choongang.commons.ExceptionProcessor;
import org.choongang.commons.Utils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController implements ExceptionProcessor {

    private final BoardConfigInfoService configInfoService;

    private final Utils utils;

    private Board board; // 게시판 설정


    /**
     * 게시판 목록
     * @param bid : 게시판 아이디
     * @param model
     * @return
     */
    @GetMapping("/list/{bid}")
    public String list(@PathVariable("bid") String bid, Model model) {
        commonProcess(bid, "list", model);

        return utils.tpl("board/list");
    }

    /**
     * 게시글 보기
     * @param seq : 게시글 번호
     * @param model
     * @return
     */
    @GetMapping("/view/{seq}")
    public String view(@PathVariable("seq") Long seq, Model model) {
        commonProcess(seq, "view", model);

        return utils.tpl("board/view");
    }

    /**
     * 게시글 작성
     * @param bid
     * @param model
     * @return
     */
    @GetMapping("/write/{bid}")
    public String write(@PathVariable("bid") String bid, Model model) {
        commonProcess(bid, "write", model);

        return utils.tpl("board/write");
    }

    /**
     * 게시글 수정
     * @param seq
     * @param model
     * @return
     */
    @GetMapping("/update/{seq}")
    public String update(@PathVariable ("seq") Long seq, Model model) {
        commonProcess(seq, "update", model);

        return utils.tpl("board/update");
    }

    /**
     * 게시글 등록, 수정
     * @param model
     * @return
     */
    @PostMapping("/save")
    public String save(Model model) {

        return null;
    }

    /**
     * 게시판의 공통 처리 - 글목록, 글쓰기 등 게시판 ID가 있는 경우
     *
     * @param bid : 게시판 ID
     * @param mode
     * @param model
     */
    private void commonProcess(String bid, String mode, Model model) {
        /* 게시판 설정 처리 S */
        if(board == null) {
            board = configInfoService.get(bid);
        }

        model.addAttribute("board", board);
        /* 게시판 설정 처리 E */
    }

    /**
     *  게시판의 공통 처리 - 게시글 보기, 게시글 수정 - 게시글 번호가 있는 경우
     *      - 게시글 조회 -> 게시판 설정
     * @param seq
     * @param mode
     * @param model
     */
    private void commonProcess(Long seq, String mode, Model model) {

    }
}
