package org.choongang.board.controllers.comment;

import lombok.RequiredArgsConstructor;
import org.choongang.board.entities.CommentData;
import org.choongang.board.service.comment.CommentInfoService;
import org.choongang.board.service.comment.CommentSaveService;
import org.choongang.commons.ExceptionRestProcessor;
import org.choongang.commons.rests.JSONData;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class ApiCommentController implements ExceptionRestProcessor {

    private final CommentInfoService commentInfoService;
    private final CommentSaveService commentSaveService;

    @GetMapping("/{seq}")
    public JSONData<CommentData> getComment(@PathVariable("seq") Long seq) {

        CommentData data = commentInfoService.get(seq);

        return new JSONData<>(data);
    }

    @PatchMapping
    public JSONData<Object> editComment(RequestComment form) {

        form.setMode("edit");
        commentSaveService.save(form);

        return new JSONData<>();
    }
}
