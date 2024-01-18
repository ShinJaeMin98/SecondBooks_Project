package org.choongang.board.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.choongang.file.entities.FileInfo;
import org.choongang.school.entities.School;

import java.util.List;
import java.util.UUID;

@Data
public class RequestBoard {
    private String mode = "write";
    private Long seq; // 게시글 번호
    private String bid; // 게시판 ID
    private String gid = UUID.randomUUID().toString();
    
    private String category; // 게시판 분류

    @NotBlank
    private String poster; // 글 작성자
    private String guestPw; // 비회원 비밀번호

    private boolean notice; // 공지사항 여부

    @NotBlank
    private String subject; // 글 제목

    @NotBlank
    private String content; // 글 내용

    // 추가필드 - 정수
    private Long num1;  // 가격
    private Long num2;  
    private Long num3;

    // 추가필드 - 한줄 텍스트
    private String text1;
    private String text2;
    private String text3;

    // 추가필드 - 여러줄 텍스트
    private String longText1;
    private String longText2;
    private String longText3;


    private List<FileInfo> editorFiles; // 에디터 파일 목록
    private List<FileInfo> attachFiles; // 첨부 파일 목록
}