package org.choongang.admin.member.controllers;


import lombok.Data;

@Data
public class MemberSearch {

    private int page = 1;

    // 한 페이지에 보여줄 멤버 수
    private int limit = 10;

//    private String userId;
//    private String name;
//    private String schoolName;
//    private String authority;

    private String sopt; // 검색옵션
    private String skey; // 검색 키워드

}
