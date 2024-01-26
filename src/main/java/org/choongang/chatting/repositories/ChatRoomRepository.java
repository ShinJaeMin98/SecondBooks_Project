package org.choongang.chatting.repositories;

import org.choongang.chatting.entities.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String>, QuerydslPredicateExecutor {

}