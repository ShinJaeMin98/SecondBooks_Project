package org.choongang.board.repositories;

import org.choongang.board.entities.CommentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CommentDataRepository extends JpaRepository<CommentData , Long>  , QuerydslPredicateExecutor<CommentData> {




}
