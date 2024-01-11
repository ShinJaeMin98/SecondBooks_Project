package org.choongang.community.freeBoard.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class freeBoardController {

    @GetMapping("/freeBoard/list")
    public String free(){

        return "freeBoard_list";
    }
}
