package org.choongang.member.service;

import org.choongang.commons.exceptions.AlertBackException;
import org.choongang.commons.exceptions.CommonException;
import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends AlertBackException {
    public MemberNotFoundException() {
        super("등록된 회원이 아닙니다.", HttpStatus.NOT_FOUND);
    }
}
