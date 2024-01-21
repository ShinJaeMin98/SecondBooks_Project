package org.choongang.board.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SaveBoardDataId {

    private Long bSeq; // 게시글 번호
    private Long mSeq; // 회원 번호
}