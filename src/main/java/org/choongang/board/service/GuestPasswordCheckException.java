package org.choongang.board.service;

import org.choongang.commons.exceptions.CommonException;
import org.springframework.http.HttpStatus;

/**
 *  비회원 비밀번호 확인이 필요한 경우
 *
 */
public class GuestPasswordCheckException extends CommonException {
    public GuestPasswordCheckException() {
        super(HttpStatus.UNAUTHORIZED);
    }
}
