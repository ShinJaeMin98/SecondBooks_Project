package org.choongang.admin.school.repositories;

import org.choongang.school.entities.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchoolRepository extends JpaRepository<School, Long> {

        List<School> findByDomain(String domain);

}
