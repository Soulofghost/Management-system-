package com.library.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookId;

    @Column(length = 20, unique = true)
    private String isbn;

    @Column(nullable = false, length = 255)
    private String bookTitle;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToOne
    @JoinColumn(name = "publisher_id", nullable = false)
    private Publisher publisher;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(length = 20)
    private String edition;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Column(nullable = false)
    private Integer totalCopies = 1;

    @Column(nullable = false)
    private Integer availableCopies = 1;

    @Column(precision = 10, scale = 2)
    private BigDecimal bookPrice;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "acquisition_date")
    private LocalDate acquisitionDate;

    @Column(length = 100)
    private String shelfLocation;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
