package org.choongang.community.freeBoard.comment;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.choongang.community.freeBoard.board.FreeBoard;

import java.time.LocalDateTime;

@Data
@Entity
public class Comment {

    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String content;
    @Column
    private LocalDateTime createDate;


}
