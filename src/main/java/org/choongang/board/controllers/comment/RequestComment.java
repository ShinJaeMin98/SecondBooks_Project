package org.choongang.board.controllers.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestComment {

    private String mode = "add";

    private Long seq=-1L;   //댓글 등록 번호

    private Long BoardDataSeq=-1L;  //댓글이 달려있는 게시글 번호

    @NotBlank
    private String commenter="";  //작성자

    @NotBlank
    private String content=""; //댓글 내용

    private String guestPw=""; //비회원 비번


}