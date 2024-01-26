package org.choongang.chatting.repositories;

import org.choongang.chatting.entities.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long>, QuerydslPredicateExecutor<ChatHistory> {
}