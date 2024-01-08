package org.choongang.file.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.commons.ExceptionProcessor;
import org.choongang.commons.exceptions.AlertBackException;
import org.choongang.commons.exceptions.CommonException;
import org.choongang.file.service.FileDeleteService;
import org.choongang.file.service.FileDownloadService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController implements ExceptionProcessor {

    private final FileDeleteService deleteService;
    private final FileDownloadService downloadService;

    @GetMapping("/delete/{seq}")
    public String delete(@PathVariable("seq") Long seq, Model model) {

        deleteService.delete(seq);

        String script = String.format("if (typeof parent.callbackFileDelete == 'function') parent.callbackFileDelete(%d);", seq);
        model.addAttribute("script", script);

        return "common/_execute_script";
    }

    @ResponseBody
    @RequestMapping("/download/{seq}")
    public void download(@PathVariable("seq") Long seq) {
        try {
            downloadService.download(seq);
        } catch (CommonException e) {
            throw new AlertBackException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}