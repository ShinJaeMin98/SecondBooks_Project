package org.choongang.chatting.service;

import lombok.RequiredArgsConstructor;
import org.choongang.chatting.controllers.RequestChatRoom;
import org.choongang.chatting.entities.ChatRoom;
import org.choongang.chatting.repositories.ChatRoomRepository;
import org.choongang.member.MemberUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomSaveService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberUtil memberUtil;
    public ChatRoom save(RequestChatRoom form) {
        String roomId = form.getRoomId();

        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseGet(() -> ChatRoom.builder()
                        .roomId(roomId)
                        .member(memberUtil.getMember())
                        .build());

        room.setRoomNm(form.getRoomNm());
        room.setCapacity(form.getCapacity());

        chatRoomRepository.saveAndFlush(room);

        return room;
    }
}