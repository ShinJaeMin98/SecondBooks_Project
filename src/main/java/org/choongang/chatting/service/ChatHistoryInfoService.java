package org.choongang.chatting.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.choongang.chatting.controllers.ChatHistorySearch;
import org.choongang.chatting.entities.ChatHistory;
import org.choongang.chatting.entities.QChatHistory;
import org.choongang.chatting.repositories.ChatHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatHistoryInfoService {
    private final ChatHistoryRepository chatHistoryRepository;
    private final EntityManager em;

    public List<ChatHistory> getList(String roomId, ChatHistorySearch search) {

        QChatHistory chatHistory = QChatHistory.chatHistory;
        BooleanBuilder andBuilder = new BooleanBuilder();

        andBuilder.and(chatHistory.chatRoom.roomId.eq(roomId));

        PathBuilder<ChatHistory> pathBuilder = new PathBuilder<>(ChatHistory.class, "chatHistory");

        List<ChatHistory> items = new JPAQueryFactory(em)
                .selectFrom(chatHistory)
                .leftJoin(chatHistory.member)
                .fetchJoin()
                .where(andBuilder)
                .orderBy(new OrderSpecifier(Order.ASC, pathBuilder.get("createdAt")))
                .fetch();

        return items;
    }
}
