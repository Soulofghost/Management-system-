package com.library.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_return")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookReturn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer returnId;

    @OneToOne
    @JoinColumn(name = "issue_id", nullable = false, unique = true)
    private BookIssue issue;

    @Column(nullable = false, name = "return_date")
    private LocalDate returnDate;

    @Column(nullable = false, name = "days_kept")
    private Integer daysKept;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isOverdue = false;

    @Column(name = "days_overdue")
    private Integer daysOverdue = 0;

    @Column(precision = 10, scale = 2)
    private BigDecimal fineAmount = BigDecimal.ZERO;

    @Column(precision = 10, scale = 2, name = "fine_per_day")
    private BigDecimal finePerDay = new BigDecimal("5.00");

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean finePaid = false;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(length = 50)
    private String paymentMethod;

    @Column(length = 50)
    private String returnCondition;

    @Column(columnDefinition = "TEXT")
    private String notes;

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
