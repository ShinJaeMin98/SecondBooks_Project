package org.choongang.school.repositories;

import org.choongang.board.entities.BoardData;
import org.choongang.school.entities.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SchoolRepository extends JpaRepository<School, Long>,
        QuerydslPredicateExecutor<School> {
    School findByDomain(String domain);
    //List<School> findBySchoolName(String name);

    boolean existsBySchoolName(String name);

}
