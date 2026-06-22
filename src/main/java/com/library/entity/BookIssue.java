package com.library.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_issue")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookIssue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer issueId;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false, name = "issue_date")
    private LocalDate issueDate;

    @Column(nullable = false, name = "due_date")
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private IssueStatus issueStatus = IssueStatus.ACTIVE;

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

    public enum IssueStatus {
        ACTIVE, RETURNED, OVERDUE
    }
}
