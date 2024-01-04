package org.choongang.commons;

import lombok.RequiredArgsConstructor;
import org.choongang.admin.config.controllers.BasicConfig;
import org.choongang.admin.config.service.ConfigInfoService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice("org.choongang")
@RequiredArgsConstructor
public class BasicConfigAdvice {
    private final ConfigInfoService infoService;

    @ModelAttribute("siteConfig")
    public BasicConfig getBasicConfig() {

        BasicConfig config = infoService.get("basic", BasicConfig.class).orElseGet(BasicConfig::new);

        return config;
    }
}
