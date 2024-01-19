package org.choongang.board.service.comment;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.board.controllers.comment.RequestComment;
import org.choongang.board.entities.Board;
import org.choongang.board.entities.BoardData;
import org.choongang.board.entities.CommentData;
import org.choongang.board.repositories.BoardDataRepository;
import org.choongang.board.repositories.CommentDataRepository;
import org.choongang.board.service.BoardDataNotFoundException;
import org.choongang.member.MemberUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CommentSaveService {

    private final CommentDataRepository commentDataRepository;
    private final BoardDataRepository boardDataRepository;
    private final MemberUtil memberUtil;
    private final PasswordEncoder encoder;
    private final HttpServletRequest request;
    private final CommentInfoService commentInfoService;


    public CommentData save(RequestComment form) {

        String mode = form.getMode();
        Long seq = form.getSeq();//댓글 번호(수정시 사용)


        mode = StringUtils.hasText(mode) ? mode : "add";

        CommentData data = null;
        if(mode.equals("edit") && seq != null){//수정
            //수정하려는 댓글 정보 가져옴
            data = commentDataRepository.findById(seq).orElseThrow(CommentNotFoundException::new);//수정하려는 댓글 정보 가져옴

        }else{//추가
            data = new CommentData();
            //게시글 번호가 수정되면 안댐//
            Long boardDataSeq = form.getBoardDataSeq();//댓글이 달려있는 게시물 번호
            BoardData boardData = boardDataRepository.findById(boardDataSeq).orElseThrow(BoardDataNotFoundException::new);

            data.setBoardData(boardData);

            data.setMember(memberUtil.getMember());//댓글 작성자 정보

            data.setIp(request.getRemoteAddr());
            data.setUa(request.getHeader("User-Agent"));

        }
        //비회원 비밀번호 존재하면 해쉬화
        String guestPw = form.getGuestPw();
        if(StringUtils.hasText(guestPw)) {
            data.setGuestPw(encoder.encode(guestPw));
        }

        data.setCommenter(form.getCommenter());
        data.setContent(form.getContent());

        commentDataRepository.saveAndFlush(data);

        commentInfoService.updateCommentCount(form.getBoardDataSeq());

        return data;
    }
}
