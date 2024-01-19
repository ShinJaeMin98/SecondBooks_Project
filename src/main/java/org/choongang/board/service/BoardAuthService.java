package org.choongang.board.service;

import jakarta.servlet.http.HttpSession;
import org.choongang.board.entities.AuthCheck;
import org.choongang.board.entities.Board;
import org.choongang.board.entities.BoardData;
import org.choongang.board.entities.CommentData;
import org.choongang.board.service.comment.CommentInfoService;
import org.choongang.board.service.config.BoardConfigInfoService;
import org.choongang.commons.Utils;
import org.choongang.commons.exceptions.AlertException;
import org.choongang.commons.exceptions.UnAuthorizedException;
import org.choongang.member.Authority;
import org.choongang.member.MemberUtil;
import org.choongang.member.entities.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class BoardAuthService {

    @Autowired
    private BoardConfigInfoService configInfoService;

    @Autowired
    private BoardInfoService infoService;

    @Autowired
    private CommentInfoService commentInfoService;

    @Autowired
    private HttpSession session;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private MemberUtil memberUtil;

    /**
     * 게시글 관련 권한 체크
     *
     * @param mode
     *          update, delete
     *
     * @param seq : 게시글 번호
     */
    public void check(String mode, Long seq) {
        if (memberUtil.isAdmin()) { // 관리자는 체크 불필요
            return;
        }

        AuthCheck data = null;
        if (mode.indexOf("comment_") != -1) { // 댓글
            data = commentInfoService.get(seq);
        } else { // 게시글
            data = infoService.get(seq);
        }


        if ((mode.contains("update") && !data.isEditable())
                || (mode.contains("delete") && !data.isDeletable())) {
            Member member = data.getMember();

            // 비회원 -> 비밀번호 확인 필요
            if (member == null) {
                session.setAttribute("mode", mode);
                session.setAttribute("seq", seq);

                throw new GuestPasswordCheckException();
            }

            // 회원인 경우 -> alert -> back
            throw new UnAuthorizedException();
        }
    }

    /**
     * 비회원 글수정, 글 삭제 비밀번호 확인
     *
     * @param password
     */
    public void validate(String password) {

        if (!StringUtils.hasText(password)) {
            throw new AlertException(Utils.getMessage("NotBlank.requestBoard.guestPw"), HttpStatus.BAD_REQUEST);
        }

        String mode = (String)session.getAttribute("mode");
        Long seq = (Long)session.getAttribute("seq");
        mode = StringUtils.hasText(mode) ? mode : "update";
        String key = null;
        if (mode.equals("update") || mode.equals("delete")) { // 비회원 게시글
            BoardData data = infoService.get(seq);

            boolean match = encoder.matches(password, data.getGuestPw());
            if (!match) {
                throw new AlertException(Utils.getMessage("Mismatch.password"), HttpStatus.BAD_REQUEST);
            }

            key = "guest_confirmed_" + seq;

        } else if (mode.equals("comment_update") || mode.equals("comment_delete")) { // 비회원 댓글
            CommentData data = commentInfoService.get(seq);

            boolean match = encoder.matches(password, data.getGuestPw());
            if (!match) {
                throw new AlertException(Utils.getMessage("Mismatch.password"), HttpStatus.BAD_REQUEST);
            }

            key = "guest_comment_confirmed_" + seq;
        }

        // 비밀번호 인증 성공 처리
        session.setAttribute(key, true);

        session.removeAttribute("mode");
        session.removeAttribute("seq");
    }

    /**
     * 글쓰기, 글보기, 글목록 접근 권한 체크
     *
     * @param bid
     */
    public void accessCheck(String mode, String bid) {
        Board board = configInfoService.get(bid);
        accessCheck(mode, board);
    }

    public void accessCheck(String mode, Board board) {

        if (memberUtil.isAdmin()) { // 관리자는 체크 불필요
            return;
        }

        if (!board.isActive()) { // 미노출 게시판
            throw new UnAuthorizedException();
        }

        boolean accessible = false;
        Authority target = Authority.ALL;
        if (mode.equals("write") || mode.equals("update")) { // 글쓰기 페이지
            target = board.getWriteAccessType();

        } else if (mode.equals("view")) { // 글보기 페이지
            target = board.getViewAccessType();

        } else if (mode.equals("list")) { // 글목록 페이지
            target = board.getListAccessType();
        }

        if (target == Authority.ALL) { // 전체 접근 가능
            accessible = true;
        }

        if (target == Authority.USER && memberUtil.isLogin()) { // 회원 + 관리자
            accessible = true;
        }

        if (target == Authority.ADMIN && memberUtil.isAdmin()) { // 관리자
            accessible = true;
        }

        if (!accessible) { // 접근 불가 페이지
            throw new UnAuthorizedException();
        }
    }
}
