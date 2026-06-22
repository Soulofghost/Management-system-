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
public class BookReturnDTO {
    private Integer returnId;
    private Integer issueId;
    private Integer bookId;
    private Integer memberId;
    private String memberCode;
    private String memberName;
    private String bookTitle;
    private LocalDate returnDate;
    private Integer daysKept;
    private Boolean isOverdue;
    private Integer daysOverdue;
    private BigDecimal fineAmount;
    private BigDecimal finePerDay;
    private Boolean finePaid;
    private LocalDate paymentDate;
    private String paymentMethod;
    private String returnCondition;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
