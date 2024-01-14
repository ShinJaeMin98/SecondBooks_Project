package org.choongang.admin.board.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.choongang.file.entities.FileInfo;

import java.util.List;
import java.util.UUID;

@Data
public class RequestBoardConfig {

    private String mode = "add";

    private String gid = UUID.randomUUID().toString();

    @NotBlank
    private String bid; // 게시판 아이디

    @NotBlank
    private String bName; // 게시판 이름

    private String schoolDomain; // 학교 이메일 도메인

    private boolean active; // 사용 여부

    private int rowsPerPage = 20; // 1페이지 게시글 수

    private int pageCountPc = 10; // PC 페이지 구간 갯수

    private int pageCountMobile = 5; // Mobile 페이지 구간 갯수

    private boolean useReply; // 답글 사용 여부

    private boolean useComment; // 댓글 사용 여부

    private boolean useEditor; // 에디터 사용 여부

    private boolean useUploadImage; // 이미지 첨부 사용 여부

    private boolean useUploadFile; // 파일 첨부 사용 여부

    private String locationAfterWriting = "list"; // 글 작성 후 이동 위치

    private String skin = "default"; // 스킨

    private String category; // 게시판 분류

    private String listAccessType = "ALL"; // 권한 설정 - 글목록

    private String viewAccessType = "ALL"; // 권한 설정 - 글보기

    private String writeAccessType = "ALL"; // 권한 설정 - 글쓰기

    private String replyAccessType = "ALL"; // 권한 설정 - 답글

    private String commentAccessType = "ALL"; // 권한 설정 - 댓글

    private String htmlTop; // 게시판 상단 HTML
    private String htmlBottom; // 게시판 하단 HTML

    private List<FileInfo> htmlTopImages; // 게시판 상단 Top 이미지

    private List<FileInfo> htmlBottomImages; // 게시판 하단 Bottom 이미지

    private FileInfo logo1; // 로고 이미지1
    private FileInfo logo2; // 로고 이미지2
    private FileInfo logo3; // 로고 이미지3
}