package org.choongang.board.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.board.entities.Board;
import org.choongang.board.service.config.BoardConfigInfoService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice("org.choongang")
@RequiredArgsConstructor
public class BoardAdvice {
    private final BoardConfigInfoService configInfoService;

    @ModelAttribute("boardMenus")
    public List<Board> getBoardList() {
        return configInfoService.getList();
    }
}
