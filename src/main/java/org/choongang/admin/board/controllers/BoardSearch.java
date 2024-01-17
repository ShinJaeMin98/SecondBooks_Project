package org.choongang.admin.board.controllers;

import lombok.Data;

import java.util.List;

@Data
public class BoardSearch {

    private int page =1;
    private int limit = 20;

    private String bid;
    private List<String> bids;  // 여러개 조회
    private String bName;
    private boolean active;

    private String sopt; // 검색옵션
    private String skey; // 검색 키워드

}
