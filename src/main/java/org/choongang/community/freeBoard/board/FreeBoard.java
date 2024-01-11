package org.choongang.community.freeBoard.board;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.choongang.community.freeBoard.comment.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class FreeBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "freeBoard", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;



}
