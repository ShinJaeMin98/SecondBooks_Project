package org.choongang.community;

import org.choongang.community.freeBoard.board.FreeBoard;
import org.choongang.community.freeBoard.board.FreeBoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest(properties = "spring.profiles.active=test")
public class FreeBoardTest {

    @Autowired
    private FreeBoardRepository freeBoardRepository;


    @Test
    @DisplayName("글 작성 테스트입니다.")
    void testJpa(){
        FreeBoard f1 = new FreeBoard();
        f1.setSubject("테스트 제목입니다.");
        f1.setContent("테스트 내용입니다.");
        f1.setCreateDate(LocalDateTime.now());
        this.freeBoardRepository.save(f1);
        System.out.println(f1.getSubject());
        System.out.println(f1.getContent());
        System.out.println(f1.getCreateDate());
    }


}
