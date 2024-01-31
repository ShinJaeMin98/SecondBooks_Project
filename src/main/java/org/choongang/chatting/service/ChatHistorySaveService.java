package org.choongang.chatting.service;

import lombok.RequiredArgsConstructor;
import org.choongang.chatting.controllers.RequestChatHistory;
import org.choongang.chatting.entities.ChatHistory;
import org.choongang.chatting.entities.ChatRoom;
import org.choongang.chatting.repositories.ChatHistoryRepository;
import org.choongang.member.MemberUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatHistorySaveService {
    private final ChatHistoryRepository chatHistoryRepository;
    private final ChatRoomInfoService chatRoomInfoService;
    private final MemberUtil memberUtil;

    public void save(RequestChatHistory form) {
        ChatRoom room = chatRoomInfoService.get(form.getRoomId());

        ChatHistory data = ChatHistory.builder()
                .member(memberUtil.getMember())
                .chatRoom(room)
                .nickName(form.getNickName())
                .message((form.getMessage()))
                .build();

        chatHistoryRepository.saveAndFlush(data);
    }

}
