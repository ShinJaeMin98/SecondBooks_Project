package org.choongang.admin.school.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.choongang.admin.menus.Menu;
import org.choongang.admin.menus.MenuDetail;
import org.choongang.admin.school.service.SchoolDeleteService;
import org.choongang.admin.school.service.SchoolSaveService;
import org.choongang.admin.school.service.SchoolSearchService;
import org.choongang.admin.school.service.SchoolVerifyService;
import org.choongang.commons.ExceptionProcessor;
import org.choongang.school.SchoolUtil;
import org.choongang.school.entities.School;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

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
    private final SchoolVerifyService verifyService;

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

    /**
     * 학교 목록/검색 목록
     * @param search
     * @param model
     * @return
     */
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

    /**
     * 학교 추가/등록 form으로 이동
     * @param mode
     * @param model
     * @param form
     * @return
     */
    @GetMapping("/add")
    public String add(@ModelAttribute String mode, Model model , RequestSchool form) {
        mode = form.getMode();
        commonProcess(mode, model);

        return "admin/school/" + mode;
    }

    /**
     * 학교 추가/등록 저장
     * @param form
     * @param errors
     * @param model
     * @return
     */
    @PostMapping("/save")
    public String save(@Valid RequestSchool form, Errors errors, Model model) {
        String mode = form.getMode();
        commonProcess(mode, model);

        String domain = form.getDomain();
        String schoolName = schoolUtil.getSchoolName(domain);
        //이미 등록된 학교일 경우
        if (!verifyService.doubleCheck(schoolName)) {
            model.addAttribute("msg" , "이미 등록된 학교입니다.");
            return "admin/school/" + mode;
        }
        //학교 등록
        saveService.save(form);

        return "redirect:/admin/school";
    }

    /**
     * 학교 삭제
     * @param num
     * @param model
     * @return
     */
    @GetMapping("/delete/{num}")
    public String delete(@PathVariable("num") Long num, Model model/*, @ModelAttribute SchoolSearch search*/) {
        //commonProcess("edit", model);
        commonProcess("list", model);

        //해당 학교 삭제
        deleteService.delete(num);

        //삭제 후 학교 목록 수집
        List<School> items = searchService.getList();
        model.addAttribute("items", items);
        return "redirect:/admin/school";
    }

    /**
     * 학교 수정 form으로 이동
     * @param num
     * @param model
     * @return
     */
    @GetMapping("/edit/{num}")
    public String edit(@PathVariable("num") Long num, Model model/*,@ModelAttribute SchoolSearch search*/) {
        commonProcess("edit", model);

        //넘어온 num에 해당하는 school값 가져옴
        School school = searchService.findSchoolByNum(num);
        //수정 form에 넘겨줄 값 세팅
        RequestSchool requestSchool = new RequestSchool(school);
        model.addAttribute("requestSchool" , requestSchool);
        model.addAttribute("num" , num);

        return "admin/school/edit";
    }

    /**
     * 수정 값 저장
     * @param model
     * @param form
     * @return
     */
    @PostMapping("/edit")
    public String edit2(Model model/*,@ModelAttribute SchoolSearch search */, RequestSchool form) {
        commonProcess("list", model);

        //수정 정보 저장
        saveService.save(form);
        //수정
        // 후 학교 목록 수집
        List<School> items = searchService.getList();
        model.addAttribute("items", items);

         return "redirect:/admin/school";
    }

    /**
     * 선택 학교 삭제
     * @param chks  : 선택된 학교 num
     * @param model
     * @return
     */
    @DeleteMapping
    public String deleteList(@RequestParam("chk") List<Long> chks, Model model) {
        commonProcess("list", model);

        //선택 학교 삭제
        deleteService.deleteChks(chks);
        //삭제 후 학교 목록 수집
        List<School> items = searchService.getList();
        model.addAttribute("items", items);

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