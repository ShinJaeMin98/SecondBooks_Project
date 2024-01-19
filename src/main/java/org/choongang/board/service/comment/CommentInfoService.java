package org.choongang.board.service.comment;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.choongang.board.controllers.comment.RequestComment;
import org.choongang.board.entities.BoardData;
import org.choongang.board.entities.CommentData;
import org.choongang.board.entities.QCommentData;
import org.choongang.board.repositories.BoardDataRepository;
import org.choongang.board.repositories.CommentDataRepository;
import org.choongang.member.MemberUtil;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.domain.Sort.Order.asc;
import static org.springframework.data.domain.Sort.Order.desc;

@Service
@RequiredArgsConstructor
public class CommentInfoService {

    private final CommentDataRepository commentDataRepository;
    private final BoardDataRepository boardDataRepository;
    private final MemberUtil memberUtil;
    

    /**
     * 댓글 단일 조회
     * @param seq
     * @return
     */
    public CommentData get(Long seq){
        CommentData data = commentDataRepository.findById(seq).orElseThrow(CommentNotFoundException::new);

        addCommentInfo(data);

        return data;

    }

    public RequestComment getForm(Long seq){
        CommentData data = get(seq);

        //뿌려줄 커맨드 객체로 변환
        RequestComment form = new ModelMapper().map(data , RequestComment.class);

        form.setBoardDataSeq(data.getBoardData().getSeq());

        return form;
    }

    /**
     * 게시글 별 댓글 목록 조회
     * @param boardDataSeq
     * @return
     */
    public List<CommentData> getList(Long boardDataSeq){

        QCommentData commentData = QCommentData.commentData;

        BooleanBuilder andBuilder = new BooleanBuilder();
        andBuilder.and(commentData.boardData.seq.eq(boardDataSeq));

        List<CommentData> items = (List<CommentData>)commentDataRepository.findAll(andBuilder, Sort.by(desc("listOrder") , asc("createdAt")));

        items.forEach(this::addCommentInfo);

        return items;
    }

    /**
     * 댓글 추가 정보 처리
     * @param data
     */
    private void addCommentInfo(CommentData data) {
        boolean editable = false, deletable = false;

        if (memberUtil.isAdmin()) { // 관리자는 댓글 수정, 삭제 제한 없음
            editable = deletable = true;
        }
    }

    /**
     * 댓글 수 업데이트
     * @param boardDataSeq
     */
    public void updateCommentCount(Long boardDataSeq){
        
        

        BoardData data = boardDataRepository.findById(boardDataSeq).orElse(null);
        
        if(data == null){
            return;
        }
        int total = commentDataRepository.getTotal(boardDataSeq);
        
        //총 댓글 수 정보 수
        data.setCommentCount(total);
        //플러쉬
        boardDataRepository.flush();
        
        
    }
    

}
