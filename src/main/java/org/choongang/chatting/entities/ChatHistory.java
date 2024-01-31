package org.choongang.chatting.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.choongang.commons.entities.Base;
import org.choongang.member.entities.Member;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ChatHistory extends Base {
    @Id @GeneratedValue
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomId")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="memberSeq")
    private Member member;

    @Column(length=40, nullable = false)
    private String nickName;

    @Column(length=500, nullable = false)
    private String message;
}