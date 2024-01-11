package org.choongang.community.freeBoard.board;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.choongang.community.freeBoard.comment.Comment;

import java.time.LocalDateTime;
import java.util.List;
@Data
@Entity
public class FreeBoard {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String subject;

    @Column
    private String content;







}
