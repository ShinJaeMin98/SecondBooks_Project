package org.choongang.board;

import org.choongang.board.repositories.BoardDataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserBoardsTest {
    @Autowired
    private BoardDataRepository repository;

    @Test
    void test1() {
        List<String>  bids = repository.getUserBoards("user01");
        System.out.println(bids);
    }
}
