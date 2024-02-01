package org.choongang.board.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.choongang.commons.entities.Base;
import org.choongang.file.entities.FileInfo;
import org.choongang.member.entities.Member;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@Entity
@NoArgsConstructor @AllArgsConstructor
@Table(indexes = {
        @Index(name="idx_boardData_basic", columnList = "notice DESC, createdAt DESC")
})
public class BoardData extends Base implements AuthCheck {
    @Id @GeneratedValue
    private Long seq;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="bid")
    private Board board;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="memberSeq")
    private Member member;

    @Column(length=65, nullable = false)
    private String gid = UUID.randomUUID().toString();

    @Column(length=60)
    private String category; // 분류

    @Column(length=40, nullable = false)
    private String poster; // 작성자

    private String guestPw; // 비회원 비밀번호

    private boolean notice;  // 공지글 여부 - true : 공지글

    @Column(nullable = false)
    private String subject;

    @Lob
    @Column(nullable = false)
    private String content;

    private int viewCount; // 조회수

    private int commentCount; // 댓글 수
    
    private int saveCount; // 찜 횟수

    private boolean editorView; // true : 에디터를 통해서 작성

    @Column(length=20)
    private String ip; // IP 주소

    @Column(length=150)
    private String ua; // User-Agent : 브라우저 정보

    private Long num1; // 추가 필드 : 정수
    private Long num2; // 추가 필드 : 정수
    private Long num3; // 추가 필드 : 정수

    @Column(length=100)
    private String text1; // 추가 필드 : 한줄 텍스트

    @Column(length=100)
    private String text2; // 추가 필드 : 한줄 텍스트

    @Column(length=100)
    private String text3; // 추가 필드 : 한줄 텍스트

    @Lob
    private String longText1; // 추가 필드 : 여러줄 텍스트

    @Lob
    private String longText2; // 추가 필드 : 여러줄 텍스트

    @Lob
    private String longText3; // 추가 필드 : 여러줄 텍스트

    @Transient
    private List<FileInfo> editorFiles; // 에디터 첨부 파일

    @Transient
    private List<FileInfo> attachFiles; // 첨부 파일

    @Transient
    private boolean editable; // 수정 가능 여부

    @Transient
    private boolean deletable; // 삭제 가능 여부

    @Transient
    private boolean mine; // 게시글 소유자

    @Transient
    private boolean showEditButton; // 수정 버튼 노출 여부

    @Transient
    private boolean showDeleteButton; // 삭제 버튼 노출 여부

    @Transient
    @JsonIgnore
    private List<CommentData> comments; // 댓글 목록
}
