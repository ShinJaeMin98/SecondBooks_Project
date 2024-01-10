package org.choongang.email;

import org.choongang.email.service.EmailMessage;
import org.choongang.email.service.EmailSendService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class EmailSendTest {
    @Autowired
    private EmailSendService emailSendService;

    @Test
    void sendTest() {
        EmailMessage message = new EmailMessage("jaemin6292@gmail.com", "제목...", "내용...");
        boolean success = emailSendService.sendMail(message);

        assertTrue(success);
    }
}