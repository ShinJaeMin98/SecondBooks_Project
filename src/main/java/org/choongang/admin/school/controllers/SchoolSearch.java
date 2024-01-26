package org.choongang.admin.school.controllers;

import lombok.Data;

@Data
public class SchoolSearch {

    private int page =1;

    //한 페이지에 보여줄 학교 수
    private int limit = 10;



/*    private Long num=-1L;
    private String sName;
    private boolean active;*/

    private String sopt; // 검색옵션
    private String skey; // 검색 키워드

}
