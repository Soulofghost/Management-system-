package com.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Integer bookId;
    private String isbn;
    private String bookTitle;
    private Integer authorId;
    private String authorName;
    private Integer publisherId;
    private String publisherName;
    private Integer categoryId;
    private String categoryName;
    private String edition;
    private LocalDate publicationDate;
    private Integer totalCopies;
    private Integer availableCopies;
    private BigDecimal bookPrice;
    private String description;
    private LocalDate acquisitionDate;
    private String shelfLocation;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
