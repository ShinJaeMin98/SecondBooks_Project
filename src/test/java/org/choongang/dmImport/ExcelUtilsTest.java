package org.choongang.dmImport;

import org.choongang.commons.ExcelUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ExcelUtilsTest {

    @Autowired
    private ExcelUtils utils;

    @Test
    @DisplayName("엑셀 파일 -> List<String[]> 으로 변환 테스트")
    void test1() {
        List<String[]> data = utils.getData("data/schools.xlsx", new int[] {0, 1}, 0);
        data.forEach(s -> System.out.println(Arrays.toString(s)));
    }

    @Test
    @DisplayName("엑셀파일 -> List<String[]> -> SQL 목록 변환 테스트")
    void test2() {
        String[] fields = { "NAME", "DOMAIN" };
        List<String> sqlData = utils.makeSql("data/schools.xlsx", new int[] {0, 1}, 0, "SCHOOLS", fields).toList();
        sqlData.forEach(System.out::println);
    }

    @Test
    @DisplayName("엑셀파일 -> List<String[]> -> SQL 파일 변환 테스트")
    void test3() {
        String destPath = "data/schools.sql";
        String[] fields = { "NAME", "DOMAIN" };
        utils.makeSql("data/schools.xlsx", new int[] {0, 1}, 0, "SCHOOLS", fields).toFile(destPath);
        File file = new File(destPath);

        assertTrue(file.exists());
    }

    @Test
    @DisplayName("엑셀파일 -> delimiter 문자열을 결합한 List<String> 변환 테스트")
    void test4() {
        List<String> data = utils.getData("data/schools.xlsx", new int[] {0, 1}, 0,"_");
        data.forEach(System.out::println);
    }
}