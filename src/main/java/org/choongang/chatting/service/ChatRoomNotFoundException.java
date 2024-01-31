package org.choongang.chatting.service;

import org.choongang.commons.Utils;
import org.choongang.commons.exceptions.AlertBackException;
import org.springframework.http.HttpStatus;

public class ChatRoomNotFoundException extends AlertBackException {
    public ChatRoomNotFoundException() {
        super(Utils.getMessage("NotFound,chatroom"),HttpStatus.NOT_FOUND);
    }
}
