package org.choongang.admin.school.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.choongang.admin.board.controllers.BoardSearch;
import org.choongang.admin.board.controllers.RequestBoardConfig;
import org.choongang.admin.menus.Menu;
import org.choongang.admin.menus.MenuDetail;
import org.choongang.admin.school.service.SchoolDeleteService;
import org.choongang.admin.school.service.SchoolSaveService;
import org.choongang.admin.school.service.SchoolSearchService;
import org.choongang.board.entities.Board;
import org.choongang.commons.ExceptionProcessor;
import org.choongang.commons.ListData;
import org.choongang.commons.Pagination;
import org.choongang.school.SchoolUtil;
import org.choongang.school.entities.School;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/school")
@RequiredArgsConstructor
public class SchoolController implements ExceptionProcessor {

    private final SchoolUtil schoolUtil;
    private final SchoolSaveService saveService;
    private final SchoolSearchService searchService;
    private final SchoolDeleteService deleteService;

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
    public String list(@ModelAttribute SchoolSearch search , Model model) {
        commonProcess("list", model);
            List<School> items = null;
        if(search.getSkey() == null || search.getSkey().equals("")){//검색어 없을 경우
            items = searchService.getList();
        } else {//검색어 있을 경우
            items = searchService.getSearchList();
        }

        model.addAttribute("items", items);
        return "admin/school/list";
    }




    @GetMapping("/add")
    public String add(@ModelAttribute String mode, Model model , RequestSchool form) {
        mode = form.getMode();

        commonProcess(mode, model);

        return "admin/school/" + mode;
    }

    @PostMapping("/save")
    public String save(@Valid RequestSchool form, Errors errors, Model model) {
        String mode = form.getMode();
        commonProcess(mode, model);

        if (errors.hasErrors()) {
            return "admin/school/" + mode;
        }
        saveService.save(form);
        return "redirect:/admin/school";
    }

    @GetMapping("/delete/{num}")
    public String delete(@PathVariable("num") Long num, Model model,@ModelAttribute SchoolSearch search) {
        commonProcess("edit", model);
        commonProcess("list", model);
        System.out.println("========================school Num:"+num);

        deleteService.delete(num);

        List<School> items = searchService.getList();
        model.addAttribute("items", items);
        return "redirect:/admin/school";
    }

    @GetMapping("/edit/{num}")
    public String edit(@PathVariable("num") Long num, Model model,@ModelAttribute SchoolSearch search) {
        commonProcess("edit", model);
        commonProcess("list", model);

        System.out.println(num+"[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[");

        School school = searchService.findSchoolByNum(num);

        RequestSchool requestSchool = new RequestSchool();
        requestSchool.setDomain(school.getDomain());
        requestSchool.setGid(school.getGid());
        requestSchool.setMenuLocation(school.getMenuLocation());
        requestSchool.setNum(school.getNum());


        model.addAttribute("requestSchool" , requestSchool);
        model.addAttribute("num" , num);

        /*List<School> items = searchService.getList();
        model.addAttribute("items", items);*/

        return "admin/school/edit";
    }

    @PostMapping("/edit")
    public String edit2(Model model,@ModelAttribute SchoolSearch search , RequestSchool form) {
        commonProcess("edit", model);
        commonProcess("list", model);
        System.out.println(form.getMode()+"666666666666666666666666666");
        saveService.save(form);

        List<School> items = searchService.getList();
        model.addAttribute("items", items);


        return "admin/school/list";
    }
    @DeleteMapping
    public String deleteList(@RequestParam("chk") List<Long> chks, Model model) {
        commonProcess("list", model);

        System.out.println(chks+"dddddddddddddddddddddddddddddddddd");
        deleteService.deleteChks(chks);
        //model.addAttribute("script", "parent.location.reload();");

        List<School> items = searchService.getList();
        model.addAttribute("items", items);

        System.out.println(items);

        model.addAttribute("script", "parent.location.reload();");
        return "common/_execute_script";
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