package com.library.service;

import com.library.dto.BookReturnDTO;
import com.library.entity.BookReturn;
import com.library.entity.BookIssue;
import com.library.entity.Book;
import com.library.repository.BookReturnRepository;
import com.library.repository.BookIssueRepository;
import com.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookReturnService {

    private final BookReturnRepository bookReturnRepository;
    private final BookIssueRepository bookIssueRepository;
    private final BookRepository bookRepository;

    @Value("${fine.per.day:5.00}")
    private BigDecimal finePerDay;

    public BookReturnDTO returnBook(BookReturnDTO bookReturnDTO, Integer userId) {
        BookIssue issue = bookIssueRepository.findById(bookReturnDTO.getIssueId())
            .orElseThrow(() -> new RuntimeException("Issue not found"));

        if (issue.getIssueStatus() != BookIssue.IssueStatus.ACTIVE) {
            throw new RuntimeException("Issue is not active");
        }

        LocalDate returnDate = bookReturnDTO.getReturnDate();
        LocalDate dueDate = issue.getDueDate();

        long daysKept = ChronoUnit.DAYS.between(issue.getIssueDate(), returnDate);
        boolean isOverdue = returnDate.isAfter(dueDate);
        int daysOverdue = isOverdue ? (int) ChronoUnit.DAYS.between(dueDate, returnDate) : 0;
        BigDecimal fineAmount = isOverdue ? finePerDay.multiply(BigDecimal.valueOf(daysOverdue)) : BigDecimal.ZERO;

        BookReturn bookReturn = new BookReturn();
        bookReturn.setIssue(issue);
        bookReturn.setReturnDate(returnDate);
        bookReturn.setDaysKept((int) daysKept);
        bookReturn.setIsOverdue(isOverdue);
        bookReturn.setDaysOverdue(daysOverdue);
        bookReturn.setFineAmount(fineAmount);
        bookReturn.setFinePerDay(finePerDay);
        bookReturn.setReturnCondition(bookReturnDTO.getReturnCondition());
        bookReturn.setNotes(bookReturnDTO.getNotes());
        bookReturn.setCreatedBy(userId);
        bookReturn.setCreatedAt(LocalDateTime.now());
        bookReturn.setUpdatedAt(LocalDateTime.now());

        BookReturn savedReturn = bookReturnRepository.save(bookReturn);

        // Update issue status
        issue.setReturnDate(returnDate);
        issue.setIssueStatus(isOverdue ? BookIssue.IssueStatus.OVERDUE : BookIssue.IssueStatus.RETURNED);
        issue.setUpdatedAt(LocalDateTime.now());
        bookIssueRepository.save(issue);

        // Increase available copies
        Book book = issue.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return mapToDTO(savedReturn);
    }

    public BookReturnDTO getReturnById(Integer returnId) {
        BookReturn bookReturn = bookReturnRepository.findById(returnId)
            .orElseThrow(() -> new RuntimeException("Return record not found"));
        return mapToDTO(bookReturn);
    }

    public BookReturnDTO getReturnByIssueId(Integer issueId) {
        BookReturn bookReturn = bookReturnRepository.findByIssueIssueId(issueId)
            .orElseThrow(() -> new RuntimeException("Return record not found for this issue"));
        return mapToDTO(bookReturn);
    }

    public List<BookReturnDTO> getOverdueBooks() {
        return bookReturnRepository.findAllByIsOverdueTrue()
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public List<BookReturnDTO> getUnpaidFines() {
        return bookReturnRepository.findAllByFinePaidFalse()
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public BookReturnDTO markFineAsPaid(Integer returnId, String paymentMethod) {
        BookReturn bookReturn = bookReturnRepository.findById(returnId)
            .orElseThrow(() -> new RuntimeException("Return record not found"));

        bookReturn.setFinePaid(true);
        bookReturn.setPaymentDate(LocalDate.now());
        bookReturn.setPaymentMethod(paymentMethod);
        bookReturn.setUpdatedAt(LocalDateTime.now());

        BookReturn updatedReturn = bookReturnRepository.save(bookReturn);
        return mapToDTO(updatedReturn);
    }

    private BookReturnDTO mapToDTO(BookReturn bookReturn) {
        BookReturnDTO dto = new BookReturnDTO();
        dto.setReturnId(bookReturn.getReturnId());
        dto.setIssueId(bookReturn.getIssue().getIssueId());
        dto.setBookId(bookReturn.getIssue().getBook().getBookId());
        dto.setMemberId(bookReturn.getIssue().getMember().getMemberId());
        dto.setMemberCode(bookReturn.getIssue().getMember().getMemberCode());
        dto.setMemberName(bookReturn.getIssue().getMember().getFirstName() + " " + bookReturn.getIssue().getMember().getLastName());
        dto.setBookTitle(bookReturn.getIssue().getBook().getBookTitle());
        dto.setReturnDate(bookReturn.getReturnDate());
        dto.setDaysKept(bookReturn.getDaysKept());
        dto.setIsOverdue(bookReturn.getIsOverdue());
        dto.setDaysOverdue(bookReturn.getDaysOverdue());
        dto.setFineAmount(bookReturn.getFineAmount());
        dto.setFinePerDay(bookReturn.getFinePerDay());
        dto.setFinePaid(bookReturn.getFinePaid());
        dto.setPaymentDate(bookReturn.getPaymentDate());
        dto.setPaymentMethod(bookReturn.getPaymentMethod());
        dto.setReturnCondition(bookReturn.getReturnCondition());
        dto.setNotes(bookReturn.getNotes());
        dto.setCreatedAt(bookReturn.getCreatedAt());
        dto.setUpdatedAt(bookReturn.getUpdatedAt());
        return dto;
    }
}
