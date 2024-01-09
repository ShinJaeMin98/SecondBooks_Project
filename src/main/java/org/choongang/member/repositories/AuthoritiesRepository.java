package org.choongang.member.repositories;

import org.choongang.member.entities.Authorities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthoritiesRepository extends JpaRepository<Authorities, Long> {
}
