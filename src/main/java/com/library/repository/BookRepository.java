package com.library.repository;

import com.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    Optional<Book> findByIsbn(String isbn);
    List<Book> findAllByIsActiveTrueOrderByBookTitleAsc();
    List<Book> findAllByAuthorAuthorIdOrderByBookTitleAsc(Integer authorId);
    List<Book> findAllByCategoryCategoryIdOrderByBookTitleAsc(Integer categoryId);
}
