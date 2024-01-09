package org.choongang.commons.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.choongang.admin.config.controllers.BasicConfig;
import org.choongang.admin.config.service.ConfigInfoService;
import org.choongang.member.MemberUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class CommonInterceptor implements HandlerInterceptor {
    private final ConfigInfoService infoService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        checkDevice(request);
        clearLoginData(request);
        loadSiteConfig(request);

        return true;
    }

    /**
     * PC, 모바일 수동 변경 처리
     *
     *  // device - PC : PC 뷰, Mobile : Mobile 뷰
     * @param request
     */
    private void checkDevice(HttpServletRequest request) {
        String device = request.getParameter("device");
        if (!StringUtils.hasText(device)) {
            return;
        }

        device = device.toUpperCase().equals("MOBILE") ? "MOBILE" : "PC";

        HttpSession session = request.getSession();
        session.setAttribute("device", device);
    }

    private void clearLoginData(HttpServletRequest request) {
        String URL = request.getRequestURI();
        if (URL.indexOf("/member/login") == -1) {
            HttpSession session = request.getSession();
            MemberUtil.clearLoginData(session);
        }
    }

    private void loadSiteConfig(HttpServletRequest request) {
        String[] excludes = {".js", ".css", ".png", ".jpg", ".jpeg", "gif", ".pdf", ".xls", ".xlxs", ".ppt"};

        String URL = request.getRequestURI().toLowerCase();

        boolean isIncluded = Arrays.stream(excludes).anyMatch(s -> URL.contains(s));
        if (isIncluded) {
            return;
        }

        BasicConfig config = infoService.get("basic", BasicConfig.class)
                .orElseGet(BasicConfig::new);

        request.setAttribute("siteConfig", config);
    }
}
