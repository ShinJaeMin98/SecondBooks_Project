package org.choongang.main.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.commons.ExceptionProcessor;
import org.choongang.commons.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequiredArgsConstructor
public class MainController implements ExceptionProcessor {

    private final Utils utils;

    @ModelAttribute("addCss")
    public String[] addCss() {
        return new String[] {"main/style"};
    }

    @GetMapping("/")
 public String index() {
     return utils.tpl("main/index");
 }
}
