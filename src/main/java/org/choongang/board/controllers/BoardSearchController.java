package org.choongang.board.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.board.entities.Board;
import org.choongang.board.entities.BoardData;
import org.choongang.board.service.BoardInfoService;
import org.choongang.commons.ExceptionProcessor;
import org.choongang.commons.ListData;
import org.choongang.commons.Utils;
import org.choongang.commons.exceptions.AlertBackException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board/search")
@RequiredArgsConstructor
public class BoardSearchController implements ExceptionProcessor {

    private final Utils utils;
    private final BoardInfoService boardInfoService;

    @ModelAttribute("pageTitle")
    public String pageTitle() {
        return Utils.getMessage("게시글_통합검색", "commons");
    }

    @ModelAttribute("addCss")
    public String[] addCss() {

        return new String[] {"board/skin_default"};
    }

    @GetMapping
    public String search(@ModelAttribute BoardDataSearch search, Model model) {

        if (!StringUtils.hasText(search.getSkey())) {
            throw new AlertBackException(Utils.getMessage("NotBlank", "skey"), HttpStatus.BAD_REQUEST);
        }

        String sopt = search.getSopt();
        if(!StringUtils.hasText(sopt)) {
            search.setSopt("SUBJECT_CONTENT");
        }

        Board board = new Board();
        board.setSkin("default");

        ListData<BoardData> data = boardInfoService.getList(search);
        model.addAttribute("items", data.getItems());
        model.addAttribute("pagination", data.getPagination());
        model.addAttribute("board", board);
        model.addAttribute("mode", "search");

        return utils.tpl("board/search");
    }
}
