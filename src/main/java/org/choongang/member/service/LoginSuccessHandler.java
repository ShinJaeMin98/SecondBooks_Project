package org.choongang.member.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.choongang.member.MemberUtil;
import org.choongang.member.entities.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();
        MemberUtil.clearLoginData(session);

        MemberInfo memberInfo = (MemberInfo) authentication.getPrincipal();
        Member member = memberInfo.getMember();
        session.setAttribute("member", member);

        String redirectURL = request.getParameter("redirectURL");
        redirectURL = StringUtils.hasText(redirectURL) ? redirectURL : "/";

        response.sendRedirect(request.getContextPath() + redirectURL);
    }
}
