package org.choongang.board.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.choongang.commons.entities.BaseMember;
import org.choongang.file.entities.FileInfo;
import org.choongang.member.Authority;

import java.util.List;
import java.util.UUID;

@Entity
@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class Board extends BaseMember {
    @Id
    @Column(length=30)
    private String bid; // 게시판 아이디

    @Column(length=65, nullable = false)
    private String gid = UUID.randomUUID().toString();

    @Column(length=60, nullable = false)
    private String bName; // 게시판 이름

    private boolean active; // 사용 여부

    private int rowsPerPage = 20; // 1페이지 게시글 수

    private int pageCountPc = 10; // PC 페이지 구간 갯수

    private int pageCountMobile = 5; // Mobile 페이지 구간 갯수

    private boolean useReply; // 답글 사용 여부

    private boolean useComment; // 댓글 사용 여부

    private boolean useEditor; // 에디터 사용 여부

    private boolean useUploadImage; // 이미지 첨부 사용 여부

    private boolean useUploadFile; // 파일 첨부 사용 여부

    @Column(length=10, nullable = false)
    private String locationAfterWriting = "list"; // 글 작성 후 이동 위치

    @Column(length=10, nullable = false)
    private String skin = "default"; // 스킨

    @Lob
    private String category; // 게시판 분류

    @Enumerated(EnumType.STRING)
    @Column(length=20, nullable = false)
    private Authority listAccessType = Authority.ALL; // 권한 설정 - 글목록

    @Enumerated(EnumType.STRING)
    @Column(length=20, nullable = false)
    private Authority viewAccessType = Authority.ALL; // 권한 설정 - 글보기

    @Enumerated(EnumType.STRING)
    @Column(length=20, nullable = false)
    private Authority writeAccessType = Authority.ALL; // 권한 설정 - 글쓰기

    @Enumerated(EnumType.STRING)
    @Column(length=20, nullable = false)
    private Authority replyAccessType = Authority.ALL; // 권한 설정 - 답글

    @Enumerated(EnumType.STRING)
    @Column(length=20, nullable = false)
    private Authority commentAccessType = Authority.ALL; // 권한 설정 - 댓글

    @Lob
    private String htmlTop; // 게시판 상단 HTML

    @Lob
    private String htmlBottom; // 게시판 하단 HTML

    @Transient
    private List<FileInfo> htmlTopImages; // 게시판 상단 Top 이미지

    @Transient
    private List<FileInfo> htmlBottomImages; // 게시판 하단 Bottom 이미지

    @Transient
    private FileInfo logo1; // 로고 이미지 1

    @Transient
    private FileInfo logo2; // 로고 이미지 2

    @Transient
    private FileInfo logo3; // 로고 이미지 3
}