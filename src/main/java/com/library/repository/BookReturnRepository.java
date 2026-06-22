package com.library.repository;

import com.library.entity.BookReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookReturnRepository extends JpaRepository<BookReturn, Integer> {
    Optional<BookReturn> findByIssueIssueId(Integer issueId);
    List<BookReturn> findAllByIsOverdueTrue();
    List<BookReturn> findAllByFinePaidFalse();
}
