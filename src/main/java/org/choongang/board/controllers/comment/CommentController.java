package org.choongang.board.controllers.comment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.choongang.board.service.comment.CommentFormValidator;
import org.choongang.board.service.comment.CommentSaveService;
import org.choongang.commons.ExceptionProcessor;
import org.choongang.commons.Utils;
import org.choongang.commons.exceptions.AlertException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController implements ExceptionProcessor {

    private final CommentFormValidator commentFormValidator;
    private  final CommentSaveService commentSaveService;


    /**
     * 댓글 저장 수정 처리
     * @param model
     * @return
     */
    @PostMapping("/save")
    public String save(@Valid RequestComment form, Errors errors, Model model){

        commonProcess(form.getMode(), model);

        commentFormValidator.validate(form, errors);

        if(errors.hasErrors()) {
            FieldError error = errors.getFieldErrors().stream().findFirst().orElse(null);
            throw new AlertException(Utils.getMessage(error.getCodes()[0]), HttpStatus.BAD_REQUEST);

        }
        commentSaveService.save(form);

        model.addAttribute("script" , "parent.location.reload()");
        return "common/_execute_script";
    }

    private void commonProcess(String mode, Model model){



    }


}
