package org.choongang.board.controllers.comment;

import lombok.RequiredArgsConstructor;
import org.choongang.board.entities.CommentData;
import org.choongang.board.service.comment.CommentInfoService;
import org.choongang.commons.ExceptionRestProcessor;
import org.choongang.commons.rests.JSONData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class ApiCommentController implements ExceptionRestProcessor {

    private final CommentInfoService commentInfoService;

    @GetMapping("/{seq}")
    public JSONData<CommentData> getComment(@PathVariable("seq") Long seq) {

        CommentData data = commentInfoService.get(seq);

        return new JSONData<>(data);
    }

}
