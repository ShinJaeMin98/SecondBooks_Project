package org.choongang.member.repositories;

import org.choongang.member.entities.Member;
import org.choongang.member.entities.QMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>,
        QuerydslPredicateExecutor<Member> {
            Optional<Member> findByEmail(String email);
            Optional<Member> findByUserId(String userId);

            default boolean existsByEmail(String email) {
                QMember member = QMember.member;

                return exists(member.email.eq(email));
            }

            default boolean existsUserId(String userId) {
                QMember member = QMember.member;

                return exists(member.userId.eq(userId));
            }
}