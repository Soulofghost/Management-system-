package com.library.repository;

import com.library.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Integer> {
    List<Publisher> findAllByIsActiveTrueOrderByPublisherNameAsc();
    List<Publisher> findAllByOrderByPublisherNameAsc();
}
