package org.choongang.myPage.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.commons.ExceptionProcessor;
import org.choongang.commons.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/myPage")
public class MyPageController implements ExceptionProcessor {

    public final Utils utils;

    @GetMapping
    public String home() {
        return utils.tpl("myPage/myPage");
    }
}
