package com.library.repository;

import com.library.entity.BookIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookIssueRepository extends JpaRepository<BookIssue, Integer> {
    List<BookIssue> findAllByMemberMemberId(Integer memberId);
    List<BookIssue> findAllByBookBookId(Integer bookId);
    List<BookIssue> findAllByIssueStatus(String status);
    Optional<BookIssue> findByMemberMemberIdAndBookBookIdAndIssueStatus(Integer memberId, Integer bookId, String status);
}
