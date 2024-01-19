package org.choongang.board.repositories;

import org.choongang.board.entities.CommentData;
import org.choongang.board.entities.QCommentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CommentDataRepository extends JpaRepository<CommentData , Long>  , QuerydslPredicateExecutor<CommentData> {

    /**
     * 댓글 수
     * @param boardDataSeq
     * @return
     */
    default int getTotal(Long boardDataSeq){
        QCommentData commentData = QCommentData.commentData;
        return (int)count(commentData.boardData.seq.eq(boardDataSeq));
    };


}
