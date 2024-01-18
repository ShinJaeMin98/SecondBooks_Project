package org.choongang.admin.school.repositories;

import org.choongang.school.entities.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {
    School findByDomain(String domain);
    //List<School> findBySchoolName(String name);

    boolean existsBySchoolName(String name);

}
