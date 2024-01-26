package org.choongang.admin.member.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.admin.menus.Menu;
import org.choongang.admin.menus.MenuDetail;
import org.choongang.admin.school.controllers.RequestSchool;
import org.choongang.commons.ExceptionProcessor;
import org.choongang.commons.ListData;
import org.choongang.member.controllers.MemberSearch;
import org.choongang.member.entities.Member;
import org.choongang.member.service.MemberInfoService;
import org.choongang.school.entities.School;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Objects;

@Controller("adminMemberController")
@RequestMapping("/admin/member")
@RequiredArgsConstructor
public class MemberController implements ExceptionProcessor {

    private final MemberInfoService infoService;

    @ModelAttribute("menuCode")
    public String getMenuCode() {
        return "member";
    }

    @ModelAttribute("subMenus")
    public List<MenuDetail> getSubMenus() {

        return Menu.getMenus("member");
    }

    @GetMapping
    public String list(@ModelAttribute MemberSearch search, Model model) {
        commonProcess("list", model);

        ListData<Member> data = infoService.getList(search);


        model.addAttribute("items", data.getItems()); // 목록
        model.addAttribute("pagination", data.getPagination()); // 페이징

        return "admin/member/list";
    }

    @GetMapping("/authority")
    public String authority(Model model) {
        commonProcess("authority", model);

        return "admin/member/authority";
    }

    /**
     * 학교 수정form으로 이동
     * @param seq
     * @param model
     * @return
     */
    @GetMapping("/edit/{seq}")
    public String edit(@PathVariable("seq") Long seq, Model model) {
        commonProcess("edit", model);



        return "admin/member/edit";
    }

    private void commonProcess(String mode, Model model) {
        mode = Objects.requireNonNullElse(mode, "list");
        String pageTitle = "회원 목록";

        if(mode.equals("authority")) { // 회원권한
            pageTitle = "회원 권한";
        }

        model.addAttribute("subMenuCode", mode);
        model.addAttribute("pageTitle", pageTitle);
    }
}


