package org.choongang.board.controllers.comment;

        import jakarta.validation.Valid;
        import lombok.RequiredArgsConstructor;
        import org.choongang.board.entities.CommentData;
        import org.choongang.board.service.comment.CommentDeleteService;
        import org.choongang.board.service.comment.CommentInfoService;
        import org.choongang.board.service.comment.CommentSaveService;
        import org.choongang.commons.ExceptionProcessor;
        import org.choongang.commons.Utils;
        import org.choongang.commons.exceptions.AlertException;
        import org.springframework.http.HttpStatus;
        import org.springframework.stereotype.Controller;
        import org.springframework.ui.Model;
        import org.springframework.validation.Errors;
        import org.springframework.validation.FieldError;
        import org.springframework.web.bind.annotation.GetMapping;
        import org.springframework.web.bind.annotation.PathVariable;
        import org.springframework.web.bind.annotation.PostMapping;
        import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController implements ExceptionProcessor {

    private final CommentFormValidator commentFormValidator;
    private final CommentSaveService commentSaveService;
    private final CommentDeleteService commentDeleteService;
    private final CommentInfoService commentInfoService;

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
        CommentData commentData = commentSaveService.save(form); // 댓글 저장, 수정

        String script = String.format("parent.location.replace('/board/view/%d?comment_id=%d');", commentData.getBoardData().getSeq(),
                commentData.getSeq());

        model.addAttribute("script" ,script);

        return "common/_execute_script";
    }

    @GetMapping("/delete/{seq}")
    public String delete(@PathVariable("seq") Long seq, Model model) {
        commonProcess("delete", model);

        Long boardDataSeq = commentDeleteService.delete(seq);

        return "redirect:/board/view/" + boardDataSeq;
    }

    private void commonProcess(String mode, Model model) {

    }
}