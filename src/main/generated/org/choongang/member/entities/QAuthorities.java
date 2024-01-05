package org.choongang.member.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAuthorities is a Querydsl query type for Authorities
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAuthorities extends EntityPathBase<Authorities> {

    private static final long serialVersionUID = -2025522100L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAuthorities authorities = new QAuthorities("authorities");

    public final EnumPath<org.choongang.member.Authority> authority = createEnum("authority", org.choongang.member.Authority.class);

    public final QMember member;

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public QAuthorities(String variable) {
        this(Authorities.class, forVariable(variable), INITS);
    }

    public QAuthorities(Path<? extends Authorities> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAuthorities(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAuthorities(PathMetadata metadata, PathInits inits) {
        this(Authorities.class, metadata, inits);
    }

    public QAuthorities(Class<? extends Authorities> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

