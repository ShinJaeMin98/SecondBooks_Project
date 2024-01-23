package org.choongang.admin.qna.controllers;

import org.choongang.admin.menus.Menu;
import org.choongang.admin.menus.MenuDetail;
import org.choongang.commons.ExceptionProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller("adminQnaController")
@RequestMapping("/admin/qna")
public class QnaController implements ExceptionProcessor {

    @ModelAttribute("menuCode")
    public String getMenuCode() { // 주 메뉴 코드
        return "qna";
    }

    @ModelAttribute("subMenus")
    public List<MenuDetail> getSubMenus() { // 서브 메뉴
        return Menu.getMenus("qna");
    }

    /**
     * 1대1 문의 목록
     * @param model
     * @return
     */
    @GetMapping
    public String list(Model model) {
        commonProcess("list", model);

        return "admin/qna/list";
    }

    /**
     * FAQ 관리
     * @param model
     * @return
     */
    @GetMapping("/faq")
    public String faq(Model model) {
        commonProcess("faq", model);

        return "admin/qna/faq";
    }

    private void commonProcess(String mode, Model model) {
        String pageTitle = "1대1문의 관리";
        mode = StringUtils.hasText(mode) ? mode : "list";

        if (mode.equals("faq")) {
            pageTitle = "FAQ 등록";
        }
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("subMenuCode", mode);
    }
}
