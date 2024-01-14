package org.choongang.school;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SchoolUtilTest {
    @Autowired
    private  SchoolUtil util;

    @Test
    void test1() {
        List<String[]> schools = util.getSchools();
        schools.forEach(s -> System.out.println(Arrays.toString(s)));

        String domain = util.getDomain("카카오");
        String schoolName = util.getSchoolName("kakao.com");

        assertEquals("카카오", schoolName);
        assertEquals("kakao.com", domain);
    }
}
