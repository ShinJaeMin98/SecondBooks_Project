package org.choongang.admin.config.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.admin.config.service.ConfigInfoService;
import org.choongang.admin.config.service.ConfigSaveService;
import org.choongang.commons.ExceptionProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/config")
@RequiredArgsConstructor
public class BasicConfigController implements ExceptionProcessor {

    private final ConfigSaveService saveService;
    private final ConfigInfoService infoService;

    @ModelAttribute("menuCode")
    public String getMenuCode() {
        return "config";
    }

    @ModelAttribute("pageTitle")
    public String getPageTitle() {
        return "기본설정";
    }

    @GetMapping
    public String index(Model model) {

        BasicConfig config = infoService.get("basic", BasicConfig.class).orElseGet(BasicConfig::new);

        model.addAttribute("basicConfig", config);

        return "admin/config/basic";
    }

    @PostMapping
    public String save(BasicConfig config, Model model) {

        saveService.save("basic", config);

        model.addAttribute("message", "저장되었습니다.");

        return "admin/config/basic";
    }
}
