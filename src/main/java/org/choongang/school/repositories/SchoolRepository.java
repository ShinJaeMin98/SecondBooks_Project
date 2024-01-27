package org.choongang.school.repositories;

import org.choongang.board.entities.BoardData;
import org.choongang.school.entities.QSchool;
import org.choongang.school.entities.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface SchoolRepository extends JpaRepository<School, Long>,
        QuerydslPredicateExecutor<School> {
    School findByDomain(String domain);
    //List<School> findBySchoolName(String name);

    default Optional<School> getByDomain(String domain) {
        return findOne(QSchool.school.domain.eq(domain));
    }

    boolean existsBySchoolName(String name);

}
