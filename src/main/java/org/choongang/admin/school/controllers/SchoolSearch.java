package org.choongang.admin.school.controllers;

import lombok.Data;

@Data
public class SchoolSearch {

    private int page =1;
    private int limit = 20;

/*    private Long num=-1L;
    private String sName;
    private boolean active;*/

    private String sopt="sName"; // 검색옵션
    private String skey; // 검색 키워드

}
