package org.choongang.sale.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.commons.Utils;

import org.choongang.commons.rests.JSONData;
import org.choongang.file.entities.FileInfo;
import org.choongang.file.service.FileUploadService;
import org.choongang.sale.entities.SaleData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sale")
public class SaleController {

    public final Utils utils;
    private final FileUploadService uploadService;
    @GetMapping("/upload")
    public String upload(/*SaleData data*/){
        //System.out.println(data);
        return utils.tpl("/sale/upload");
    }


    @PostMapping("/upload")
    public String upload(SaleData data) {

        System.out.println(data);//확인용

        return utils.tpl("/sale/test");
    }




}
