package org.choongang.school;

import org.choongang.commons.ExcelUtils;
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
    @Autowired
    private ExcelUtils excelUtils;

    @Test
    void test1() {
        List<String[]> schools = util.getSchools();
        schools.forEach(s -> System.out.println(Arrays.toString(s)));

        String domain = util.getDomain("카카오");
        String schoolName = util.getSchoolName("kakao.com");

        assertEquals("카카오", schoolName);
        assertEquals("kakao.com", domain);
    }

    @Test
    void test2() {

            List<String[]> list = excelUtils.getData("data/schools.xlsx", new int[] {0, 1}, 0);
            String schools = "";
            int k = 0;
            for(String[] l : list){
                //l[0] : 학교명 , l[1] : 도메인
                int i = l[0].indexOf(" ");
                if(i != -1){
                    l[0] = l[0].substring(0,i).trim();
                }
                l[1] = l[1].replace("www.","");
                l[1] = l[1].replaceAll("/" , "");

                int v = schools.indexOf(l[0]);

                if(!l[1].equals("") || l[1] != null){
                    if(v == -1){
                        schools += k+l[0]+"_"+l[1]+"\n";
                    }
                }
                k++;
                if(k == 225){
                    break;
                }
            }
            System.out.println(schools);

        }
    }

