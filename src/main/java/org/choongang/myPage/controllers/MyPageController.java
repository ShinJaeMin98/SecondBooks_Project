package org.choongang.myPage.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.board.controllers.BoardDataSearch;
import org.choongang.board.entities.BoardData;
import org.choongang.board.service.BoardInfoService;
import org.choongang.board.service.SaveBoardDataService;
import org.choongang.commons.ExceptionProcessor;
import org.choongang.commons.ListData;
import org.choongang.commons.Utils;
import org.choongang.member.MemberUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/myPage")
public class MyPageController implements ExceptionProcessor {

    private final SaveBoardDataService saveBoardDataService;
    private final BoardInfoService boardInfoService;
    private final MemberUtil memberUtil;
    public final Utils utils;

    @GetMapping // 마이페이지 메인
    public String index(Model model) {
        commonProcess("main", model);

        return utils.tpl("myPage/myPage");
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

    @GetMapping("/my_post")
    public String myPost(@ModelAttribute BoardDataSearch search, Model model) {
        commonProcess("my_post", model);

        search.setUserId(memberUtil.getMember().getUserId());
        ListData<BoardData> data = boardInfoService.getList(search);
        model.addAttribute("items", data.getItems());
        model.addAttribute("pagination", data.getPagination());

        return utils.tpl("myPage/my_post");
    }

    private void commonProcess(String mode, Model model) {
        mode = StringUtils.hasText(mode) ? mode : "main";
        String pageTitle = Utils.getMessage("마이페이지", "commons");

        List<String> addCss = new ArrayList<>();
        List<String> addScript = new ArrayList<>();

        List<String> addCommonScript = new ArrayList<>();


        if (mode.equals("save_post")) { // 찜한 게시글 페이지
            pageTitle = Utils.getMessage("찜_게시글", "commons");

        }

        if (mode.equals("my_post")) { // 찜한 게시글 페이지
            pageTitle = Utils.getMessage("내가_쓴_글", "commons");

        }

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("addCss", addCss);
        model.addAttribute("addScript", addScript);
        model.addAttribute("addCommonScript", addCommonScript);
    }
}
