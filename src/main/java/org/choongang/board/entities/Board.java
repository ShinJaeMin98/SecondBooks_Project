package org.choongang.board.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.choongang.commons.entities.BaseMember;

@Entity
@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class Board extends BaseMember {
    @Id
    private String bid; // 게시판 아이디

    private String bName; // 게시판 이름

    private boolean active; // 사용 여부

    private int rowsPerPage = 20;
}