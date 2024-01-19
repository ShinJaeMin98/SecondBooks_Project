package org.choongang.board.controllers.comment;

import lombok.RequiredArgsConstructor;
import org.choongang.commons.ExceptionProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController implements ExceptionProcessor {

    /**
     * 댓글 저장 수정 처리
     * @param model
     * @return
     */
    @PostMapping("/save")
    public String save(Model model){

        model.addAttribute("script" , "parent.location.reload()");
        return "common/_execute_script";
    }

    private void commonProcess(String mode, Model model){



    }


}
