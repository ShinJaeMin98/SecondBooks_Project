package org.choongang.admin.school.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.choongang.admin.menus.Menu;
import org.choongang.admin.menus.MenuDetail;
import org.choongang.commons.ExceptionProcessor;
import org.choongang.school.SchoolUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/school")
@RequiredArgsConstructor
public class SchoolController implements ExceptionProcessor {

    private final SchoolUtil schoolUtil;

    @ModelAttribute("menuCode")
    public String getMenuCode() {
        return "school";
    }

    @ModelAttribute("subMenus")
    public List<MenuDetail> getSubMenus() {
        return Menu.getMenus("school");
    }

    @ModelAttribute("schools")
    public List<String[]> getSchools() {
        return schoolUtil.getSchools();
    }

    @GetMapping
    public String list(Model model) {
        commonProcess("list", model);

        return "admin/school/list";
    }

    @GetMapping("/add")
    public String add( Model model, RequestSchool requestSchool) {
        String mode = requestSchool.getMode();
        commonProcess(mode, model);
        return "admin/school/"+mode;
    }

    @PostMapping("/save")
    public String save(@Valid RequestSchool form, Errors errors, Model model) {
        String mode = form.getMode();
        commonProcess(mode, model);

        if (errors.hasErrors()) {
            return "admin/school/" + mode;
        }

        return "redirect:/admin/school";
    }

    private void commonProcess(String mode, Model model) {
        mode = StringUtils.hasText(mode) ? mode : "list";
        String pageTitle = "학교 목록";

        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript = new ArrayList<>();

        if (mode.equals("add") || mode.equals("edit")) {
            pageTitle = mode.equals("edit") ? "학교 정보 수정" : "학교 정보 등록";
            addCommonScript.add("fileManager");
            addScript.add("school/form");
        }

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("subMenuCode", mode);

        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
    }
}