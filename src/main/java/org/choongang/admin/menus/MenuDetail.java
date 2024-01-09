package org.choongang.admin.menus;

public record MenuDetail(
        String code, // 하위 메뉴 코드
        String name, // 메뉴 명
        String url // 메뉴 URL
) {}
