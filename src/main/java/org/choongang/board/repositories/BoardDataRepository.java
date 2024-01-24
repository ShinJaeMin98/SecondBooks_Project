package org.choongang.board.repositories;

import org.choongang.board.entities.BoardData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardDataRepository extends JpaRepository<BoardData, Long>,
                                                QuerydslPredicateExecutor<BoardData> {
    @Query("SELECT DISTINCT b.board.bid FROM BoardData b WHERE b.member.userId=:userId")
    List<String> getUserBoards(@Param("userId") String userId);


}
