package org.choongang.community.freeBoard.board;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFreeBoard is a Querydsl query type for FreeBoard
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFreeBoard extends EntityPathBase<FreeBoard> {

    private static final long serialVersionUID = 527596541L;

    public static final QFreeBoard freeBoard = new QFreeBoard("freeBoard");

    public final ListPath<org.choongang.community.freeBoard.comment.Comment, org.choongang.community.freeBoard.comment.QComment> commentList = this.<org.choongang.community.freeBoard.comment.Comment, org.choongang.community.freeBoard.comment.QComment>createList("commentList", org.choongang.community.freeBoard.comment.Comment.class, org.choongang.community.freeBoard.comment.QComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath subject = createString("subject");

    public QFreeBoard(String variable) {
        super(FreeBoard.class, forVariable(variable));
    }

    public QFreeBoard(Path<? extends FreeBoard> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFreeBoard(PathMetadata metadata) {
        super(FreeBoard.class, metadata);
    }

}

