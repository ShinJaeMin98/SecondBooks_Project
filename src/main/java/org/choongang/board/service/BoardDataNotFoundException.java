package org.choongang.board.service;

import org.choongang.commons.Utils;
import org.choongang.commons.exceptions.AlertException;
import org.springframework.http.HttpStatus;

public class BoardDataNotFoundException extends AlertException {
    public BoardDataNotFoundException() {
        super(Utils.getMessage("NotFound.boardData", "errors"), HttpStatus.NOT_FOUND);
    }
}
