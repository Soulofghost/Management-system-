package com.library.repository;

import com.library.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findByMemberCode(String memberCode);
    Optional<Member> findByEmail(String email);
    List<Member> findAllByIsActiveTrueOrderByFirstNameAsc();
    List<Member> findAllByOrderByFirstNameAsc();
}
