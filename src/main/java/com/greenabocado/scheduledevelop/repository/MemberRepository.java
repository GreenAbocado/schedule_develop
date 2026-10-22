package com.greenabocado.scheduledevelop.repository;

import com.greenabocado.scheduledevelop.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
}
