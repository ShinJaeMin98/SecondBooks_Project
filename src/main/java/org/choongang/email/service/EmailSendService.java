package org.choongang.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmailSendService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    /**
     * 메일 전송
     *
     * @param message : 수신인, 메일 제목, 메일 내용
     * @param tpl : 템플릿 사용하는 경우 템플릿 이름
     * @param tplData : 치환코드
     * @return
     */
    public boolean sendMail(EmailMessage message, String tpl, Map<String, Object> tplData) {
        String text = null;
        /**
         * 이메일 템플릿 사용하는 경우 EmailMessage의 제목, 내용, 수신인 및 tplData 추가 치환 속성을 전달하고
         * 타임리프로 번역된 텍스트를 반환 값으로 처리
         */
        if (StringUtils.hasText(tpl)) {
            tplData = Objects.requireNonNullElse(tplData, new HashMap<>());
            Context context = new Context();

            tplData.put("to", message.to());
            tplData.put("subject", message.subject());
            tplData.put("message", message.message());

            context.setVariables(tplData);

            text = templateEngine.process("email/" + tpl, context);
        } else { // 템플릿 전송이 아닌 경우 메세지로 대체
            text = message.message();
        }

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(message.to()); // 메일 수신자
            mimeMessageHelper.setSubject(message.subject());  // 메일 제목
            mimeMessageHelper.setText(text, true); // 메일 내용
            javaMailSender.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return false;

    }

    public boolean sendMail(EmailMessage message) {
        return sendMail(message, null, null);
    }
}