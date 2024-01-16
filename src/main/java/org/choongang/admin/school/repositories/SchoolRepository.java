package org.choongang.admin.school.repositories;

import org.choongang.admin.config.entities.Configs;
import org.choongang.school.entities.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {
}
