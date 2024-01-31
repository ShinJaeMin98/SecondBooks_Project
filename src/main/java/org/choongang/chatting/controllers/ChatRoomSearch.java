package org.choongang.chatting.controllers;

import lombok.Data;

@Data
public class ChatRoomSearch {
    private int page = 1;
    private int limit = 20;

    private String sopt; // 검색 옵션
    private String skey; // 검색 키워드
}
