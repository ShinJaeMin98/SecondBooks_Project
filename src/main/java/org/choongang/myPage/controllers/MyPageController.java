package org.choongang.myPage.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.board.controllers.BoardDataSearch;
import org.choongang.board.entities.BoardData;
import org.choongang.board.service.SaveBoardDataService;
import org.choongang.commons.ExceptionProcessor;
import org.choongang.commons.ListData;
import org.choongang.commons.RequestPaging;
import org.choongang.commons.Utils;
import org.choongang.member.entities.Member;
import org.choongang.member.service.follow.FollowBoardService;
import org.choongang.member.service.follow.FollowService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/myPage")
public class MyPageController implements ExceptionProcessor {

    private final SaveBoardDataService saveBoardDataService;
    private final FollowBoardService followBoardService;
    private final FollowService followService;

    public final Utils utils;

    @GetMapping // 마이페이지 메인
    public String index(Model model) {
        commonProcess("main", model);

        return utils.tpl("myPage/index");
    }

    /**
     * 찜 게시글 목록
     *
     * @param search
     * @param model
     * @return
     */
    @GetMapping("/save_post")
    public String savePost(@ModelAttribute BoardDataSearch search, Model model) {
        commonProcess("save_post", model);

        ListData<BoardData> data = saveBoardDataService.getList(search);

        model.addAttribute("items", data.getItems());
        model.addAttribute("pagination", data.getPagination());

        return utils.tpl("myPage/save_post");
    }

    @GetMapping("/follow")
    public String followList(@RequestParam(name="mode", defaultValue = "follower") String mode, RequestPaging paging, Model model) {
        commonProcess("follow", model);

        ListData<Member> data = followService.getList(mode, paging);

        model.addAttribute("items", data.getItems());
        model.addAttribute("pagination", data.getPagination());
        model.addAttribute("mode", mode);

        return utils.tpl("myPage/follow");
    }


    @GetMapping("/follow/{userId}")
    public String followBoard(@PathVariable("userId") String userId,
                              @RequestParam(name="mode", defaultValue="follower") String mode,
                              @ModelAttribute BoardDataSearch search, Model model) {

        // 전체 조회가 아니라면 아이디별 조회
        if (!userId.equals("all")) {
            search.setUserId(userId);
        } else {
            search.setUserId(null);
        }

        ListData<BoardData> data = followBoardService.getList(mode, search);

        model.addAttribute("items", data.getItems());
        model.addAttribute("pagination", data.getPagination());

        return utils.tpl("myPage/follow_board");
    }

    private void commonProcess(String mode, Model model) {
        mode = StringUtils.hasText(mode) ? mode : "main";
        String pageTitle = Utils.getMessage("마이페이지", "commons");

        List<String> addCss = new ArrayList<>();
        List<String> addScript = new ArrayList<>();

        List<String> addCommonScript = new ArrayList<>();


        if (mode.equals("save_post")) { // 찜한 게시글 페이지
            pageTitle = Utils.getMessage("찜_게시글", "commons");

            addScript.add("board/common");
            addScript.add("myPage/save_post");
        } else if (mode.equals("follow")) {
            addCommonScript.add("follow");
        }

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("addCss", addCss);
        model.addAttribute("addScript", addScript);
        model.addAttribute("addCommonScript", addCommonScript);
    }
}
