package org.choongang.board.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.choongang.board.entities.BoardData;
import org.choongang.commons.Utils;
import org.choongang.commons.exceptions.AlertException;
import org.choongang.commons.exceptions.UnAuthorizedException;
import org.choongang.member.entities.Member;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class BoardAuthService {

    private final BoardInfoService infoService;
    private final HttpSession session;

    /**
     * 게시글 관련 권한 체크
     *
     * @param mode  (update, delete)
     * @param seq   : 게시글 번호
     */
    public void check(String mode, Long seq) {
        BoardData data = infoService.get(seq);

        if ((mode.equals("update") && !data.isEditable()) || (mode.equals("delete") && !data.isDeletable())) {

            Member member = data.getMember();

            // 비회원 -> 비밀번호 확인 필요
            if (member == null) {
                session.setAttribute("mode", mode);
                session.setAttribute("seq", seq);
                throw new GuestPasswordCheckException();
            }
            // 회원 -> alert -> back
            throw new UnAuthorizedException();
        }
    }

    /**
     * 비회원 글수정, 글삭제 비밀번호 확인
     *
     * @param password
     */
    public void validate(String password) {
        if (!StringUtils.hasText(password)) {
            throw new AlertException(Utils.getMessage("NotBlank.requestBoard.guestPw"), HttpStatus.BAD_REQUEST);
        }

        String mode = (String) session.getAttribute("mode");
        Long seq = (Long)session.getAttribute("seq");
    }
}
