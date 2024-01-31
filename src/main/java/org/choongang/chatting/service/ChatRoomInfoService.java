package org.choongang.chatting.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.chatting.controllers.ChatRoomSearch;
import org.choongang.chatting.entities.ChatRoom;
import org.choongang.chatting.entities.QChatRoom;
import org.choongang.chatting.repositories.ChatRoomRepository;
import org.choongang.commons.ListData;
import org.choongang.commons.Pagination;
import org.choongang.commons.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static org.springframework.data.domain.Sort.Order.desc;

@Service
@RequiredArgsConstructor
public class ChatRoomInfoService {

    private final ChatRoomRepository chatRoomRepository;
    private final HttpServletRequest request;

    public ChatRoom get(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(ChatRoomNotFoundException::new);

        return chatRoom;
    }

    public ListData<ChatRoom> getList(ChatRoomSearch search) {
        int page = Utils.onlyPositiveNumber(search.getPage(), 1);
        int limit = Utils.onlyPositiveNumber(search.getLimit(), 20);

        QChatRoom chatRoom = QChatRoom.chatRoom;
        BooleanBuilder andBuilder = new BooleanBuilder();

        /* 검색 조건 처리 S */
        String sopt = search.getSopt();
        String skey = search.getSkey();
        sopt = StringUtils.hasText(sopt) ? sopt : "all";
        if (StringUtils.hasText(skey)) {
            skey = skey.trim();
            BooleanExpression roomNmCond = chatRoom.roomNm.contains(skey);
            BooleanExpression roomIdCond = chatRoom.roomId.contains(skey);

            if (sopt.equals("roomNm")) {
                andBuilder.and(roomNmCond);
            } else if (sopt.equals("roomId")) {
                andBuilder.and(roomIdCond);
            } else {
                BooleanBuilder orBuilder = new BooleanBuilder();
                orBuilder.or(roomNmCond).or(roomIdCond);
                andBuilder.and(orBuilder);
            }
        }

        /* 검색 조건 처리 E */

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("createdAt")));

        Page<ChatRoom> data = chatRoomRepository.findAll(andBuilder, pageable);

        int total = (int) chatRoomRepository.count(andBuilder);

        Pagination pagination = new Pagination(page, total, 10, limit, request);

        return new ListData<>(data.getContent(), pagination);
    }
}