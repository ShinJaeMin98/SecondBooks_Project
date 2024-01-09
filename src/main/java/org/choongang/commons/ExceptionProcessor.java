package org.choongang.commons;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.choongang.commons.exceptions.AlertBackException;
import org.choongang.commons.exceptions.AlertException;
import org.choongang.commons.exceptions.CommonException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;

public interface ExceptionProcessor {


    @ExceptionHandler(Exception.class)
    default String errorHandler(Exception e, HttpServletResponse response, HttpServletRequest request, Model model) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500

        if (e instanceof CommonException) {
            CommonException commonException = (CommonException) e;
            status = commonException.getStatus();
        }

        response.setStatus(status.value());

        e.printStackTrace();

        if (e instanceof AlertException) { // 자바스크립트 Alert형태로 응답
            String script = String.format("alert('%s');", e.getMessage());

            if (e instanceof AlertBackException) { // history.back(); 실행
                script += "history.back();";
            }

            model.addAttribute("script", script);
            return "common/_execute_script";
        }

        model.addAttribute("status", status.value());
        model.addAttribute("path", request.getRequestURI());
        model.addAttribute("method", request.getMethod());
        model.addAttribute("message", e.getMessage());

        return "error/common";
    }
}
