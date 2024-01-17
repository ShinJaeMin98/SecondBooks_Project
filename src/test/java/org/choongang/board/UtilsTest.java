package org.choongang.board;

import org.choongang.commons.Utils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UtilsTest {

    @Autowired
    private Utils utils;

    @Test
    void test1() {
        String data = utils.confirmDelete();
        System.out.println(data);
    }
}
