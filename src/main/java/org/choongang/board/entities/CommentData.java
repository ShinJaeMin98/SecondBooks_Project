package org.choongang.board.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.choongang.commons.entities.Base;
import org.choongang.member.entities.Member;

import java.lang.reflect.Type;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = @Index(name = "idx_comment_basic" , columnList = "listOrder DESC , createdAt ASC"))
public class CommentData extends Base {

    @Id @GeneratedValue
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="boardDataSeq")
    private BoardData boardData;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberSeq")
    private Member member;


    @Column(length = 40 , nullable = false)
    private String commenter;   //작성자

    @Column(length = 65)
    private String guestPw; //비회원 댓글 수정/삭제 비밀번호

    @Lob
    @Column(nullable = false)
    private String content; //댓글 내용

    @Column(length = 20)
    private String ip; //작성자  ip주소

    @Column(length = 120)
    private String ua;  //작성자 user-Agent 정보 (브라우저 정보?)


    private long listOrder;     //댓글 1차 정렬 기준
    private int depth;  //대댓글 들여쓰기 정도







}
