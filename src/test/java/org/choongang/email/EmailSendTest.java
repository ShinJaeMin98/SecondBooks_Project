package org.choongang.email;

import org.choongang.email.service.EmailMessage;
import org.choongang.email.service.EmailSendService;
import org.choongang.email.service.EmailVerifyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class EmailSendTest {

    @Autowired
    private EmailVerifyService emailVerifyService;

    @Test
    @DisplayName("이메일 인증 번호 전송 테스트")
    void emailVerifyTest() {
        boolean result = emailVerifyService.sendCode("jaemin6292@gmail.com");
        assertTrue(result);
    }

}