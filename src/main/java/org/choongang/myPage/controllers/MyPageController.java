package org.choongang.myPage.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.commons.ExceptionProcessor;
import org.choongang.commons.Utils;
import org.choongang.member.menus.Menu;
import org.choongang.member.menus.MenuDetail;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/myPage")
@RequiredArgsConstructor
public class MyPageController implements ExceptionProcessor {

    private final Utils utils;

    @ModelAttribute("menuCode")
    public String getMenuCode() {
        return "myPage";
    }

    @ModelAttribute("subMenus")
    public List<MenuDetail> getSubMenus() {
        return Menu.getMenus("myPage");
    }

    @GetMapping
    public String profile(Model model) {
        commonProcess("profile", model);

        return utils.tpl("myPage/profile");
    }


    @GetMapping("/sellingList")
    public String sellingList(Model model){
        commonProcess("sellingList", model);
        return utils.tpl("myPage/sellingList");
    }
    @GetMapping("/successList")
    public String successList(Model model){
        commonProcess("successList", model);
        return utils.tpl("myPage/successList");
    }
    @GetMapping("/likeList")
    public String likeList(Model model){
        commonProcess("likeList", model);
        return utils.tpl("myPage/likeList");
    }
    @GetMapping("/boardList")
    public String boardList(Model model){
        commonProcess("boardList", model);
        return utils.tpl("myPage/boardList");
    }

    private void commonProcess(String mode, Model model) {
        String pageTitle = "마이페이지";
        mode = StringUtils.hasText(mode) ? mode : "list";

        if (mode.equals("sellingList")) {
            pageTitle = "판매중";
        } else if (mode.equals("successList")) {
            pageTitle = "거래완료";
        } else if (mode.equals("likeList")) {
            pageTitle = "찜목록";
        } else if (mode.equals("boardList")) {
            pageTitle = "작성 글목록";
        }
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("subMenuCode", mode);
    }
}
