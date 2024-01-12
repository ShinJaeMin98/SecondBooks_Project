package org.choongang.admin.config.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.admin.config.service.ConfigInfoService;
import org.choongang.admin.config.service.ConfigSaveService;
import org.choongang.admin.menus.Menu;
import org.choongang.admin.menus.MenuDetail;
import org.choongang.commons.ExceptionProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/config/school")
@RequiredArgsConstructor
public class SchoolConfigController implements ExceptionProcessor {

    private final ConfigInfoService infoService;
    private final ConfigSaveService saveService;

    @ModelAttribute("menuCode")
    public String getMenuCode() {
        return "config";
    }

    @ModelAttribute("subMenus")
    public List<MenuDetail> getSubMenus() {
        return Menu.getMenus("config");
    }

    @ModelAttribute("subMenuCode")
    public String getSubMenuCode() {

        return "school";
    }

    @ModelAttribute("pageTitle")
    public String getPageTitle() {
        return "학교설정";
    }

    @GetMapping
    public String index(Model model) {

        SchoolConfig config = infoService.get("school_config", SchoolConfig.class).orElseGet(SchoolConfig::new);

        model.addAttribute("schoolConfig", config);

        return "admin/config/school";
    }

    @PostMapping
    public String save(SchoolConfig config, Model model) {

        saveService.save("school_config", config);

        model.addAttribute("message", "저장 되었습니다.");

        return "admin/config/school";
    }
}