package com.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookIssueDTO {
    private Integer issueId;
    private Integer memberId;
    private String memberCode;
    private String memberName;
    private Integer bookId;
    private String bookTitle;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String issueStatus;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
