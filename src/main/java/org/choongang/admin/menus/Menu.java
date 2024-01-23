package org.choongang.admin.menus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menu {
    private final static Map<String, List<MenuDetail>> menus;

    static {
        menus = new HashMap<>();

        menus.put("config", Arrays.asList(
                new MenuDetail("basic", "기본설정", "/admin/config"),
                new MenuDetail("school", "학교설정", "/admin/config/school")
        ));

        menus.put("member", Arrays.asList(
                new MenuDetail("list", "회원목록", "/admin/member"),
                new MenuDetail("authority", "회원권한", "/admin/member/authority")
        ));

        menus.put("school", Arrays.asList(
                new MenuDetail("list", "학교 목록", "/admin/school"),
                new MenuDetail("add", "학교 정보 등록", "/admin/school/add")
        ));


        menus.put("board", Arrays.asList(
                new MenuDetail("list", "게시판목록", "/admin/board"),
                new MenuDetail("add", "게시판등록", "/admin/board/add"),
                new MenuDetail("posts", "게시글관리", "/admin/board/posts")
        ));

        menus.put("qna", Arrays.asList(
                new MenuDetail("list", "질의응답목록", "/admin/qna"),
                new MenuDetail("faq", "FAQ등록", "/admin/qna/faq")
        ));
    }

    public static List<MenuDetail> getMenus(String code) {
        return menus.get(code);
    }
}