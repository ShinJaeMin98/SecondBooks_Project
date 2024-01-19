package org.choongang.board.service.comment;

import org.choongang.commons.Utils;
import org.choongang.commons.exceptions.AlertBackException;
import org.springframework.http.HttpStatus;

public class CommentNotFoundException extends AlertBackException{

    public CommentNotFoundException(){
        super(Utils.getMessage("NotFound.comment" , "errors") , HttpStatus.NOT_FOUND);
    }


}
