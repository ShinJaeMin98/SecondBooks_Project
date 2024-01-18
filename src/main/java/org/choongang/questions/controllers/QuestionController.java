package org.choongang.questions.controllers;

import org.choongang.admin.menus.Menu;
import org.choongang.admin.menus.MenuDetail;
import org.choongang.commons.ExceptionProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/questions")
public class QuestionController implements ExceptionProcessor {

    @ModelAttribute("menuCode")
    public String getMenuCode() { return "questions"; }

    @ModelAttribute("subMenus")
    public List<MenuDetail> getSubMenus() { return Menu.getMenus("questions");}

    @GetMapping
    public String list(Model model) {
        commonProcess("list", model);

        return "list";
    }

    @GetMapping("/faq")
    public String faq(Model model) {
        commonProcess("faq", model);

        return "faq";
    }

    private void commonProcess(String mode, Model model)
}
