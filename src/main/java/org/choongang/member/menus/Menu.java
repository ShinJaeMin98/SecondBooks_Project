package org.choongang.member.menus;



import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menu {
    private final static Map<String, List<MenuDetail>> menus;

    static {
        menus = new HashMap<>();
        /*menus.put("/", Arrays.asList(
                new MenuDetail("products","책방","/products"),
                new MenuDetail("boards","게시판","/boards"),
                new MenuDetail("questions", "FAQ", "/questions"),
                new MenuDetail("myPage", "마이페이지", "/myPage")
        ));*/

        menus.put("products", Arrays.asList(
                new MenuDetail("sale","팔아요","/products/sell"),
                new MenuDetail("buy", "구해요", "/products/buy")
        ));

        menus.put("boards", Arrays.asList(
                new MenuDetail("community", "자유게시판", "/boards/community"),
                new MenuDetail("study", "스터디", "/boards/study")
        ));

        menus.put("questions", Arrays.asList(
                new MenuDetail("faq", "FAQ", "/questions/faq"),
                new MenuDetail("qna", "Q&A", "/questions/qna")
        ));

        menus.put("myPage", Arrays.asList(
                new MenuDetail("profile", "프로필", "/myPage"),
                new MenuDetail("sellingList", "판매중", "/myPage/sellingList"),
                new MenuDetail("successList", "거래완료", "/myPage/successList"),
                new MenuDetail("likeList", "찜","/myPage/likeList"),
                new MenuDetail("boardList", "작성글", "/myPage/boardList")
        ));


    }

    public static List<MenuDetail> getMenus(String code) {
        return menus.get(code);
    }
}
