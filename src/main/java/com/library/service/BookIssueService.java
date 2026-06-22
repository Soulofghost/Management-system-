package com.library.service;

import com.library.dto.BookIssueDTO;
import com.library.entity.BookIssue;
import com.library.entity.Member;
import com.library.entity.Book;
import com.library.repository.BookIssueRepository;
import com.library.repository.MemberRepository;
import com.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookIssueService {

    private final BookIssueRepository bookIssueRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    public BookIssueDTO issueBook(BookIssueDTO bookIssueDTO, Integer userId) {
        Member member = memberRepository.findById(bookIssueDTO.getMemberId())
            .orElseThrow(() -> new RuntimeException("Member not found"));
        
        Book book = bookRepository.findById(bookIssueDTO.getBookId())
            .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("Book is not available");
        }

        BookIssue issue = new BookIssue();
        issue.setMember(member);
        issue.setBook(book);
        issue.setIssueDate(bookIssueDTO.getIssueDate());
        issue.setDueDate(bookIssueDTO.getDueDate());
        issue.setIssueStatus(BookIssue.IssueStatus.ACTIVE);
        issue.setNotes(bookIssueDTO.getNotes());
        issue.setCreatedBy(userId);
        issue.setCreatedAt(LocalDateTime.now());
        issue.setUpdatedAt(LocalDateTime.now());

        BookIssue savedIssue = bookIssueRepository.save(issue);

        // Decrease available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        return mapToDTO(savedIssue);
    }

    public BookIssueDTO getIssueById(Integer issueId) {
        BookIssue issue = bookIssueRepository.findById(issueId)
            .orElseThrow(() -> new RuntimeException("Issue not found"));
        return mapToDTO(issue);
    }

    public List<BookIssueDTO> getAllActiveIssues() {
        return bookIssueRepository.findAllByIssueStatus("ACTIVE")
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public List<BookIssueDTO> getMemberIssues(Integer memberId) {
        return bookIssueRepository.findAllByMemberMemberId(memberId)
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    private BookIssueDTO mapToDTO(BookIssue issue) {
        BookIssueDTO dto = new BookIssueDTO();
        dto.setIssueId(issue.getIssueId());
        dto.setMemberId(issue.getMember().getMemberId());
        dto.setMemberCode(issue.getMember().getMemberCode());
        dto.setMemberName(issue.getMember().getFirstName() + " " + issue.getMember().getLastName());
        dto.setBookId(issue.getBook().getBookId());
        dto.setBookTitle(issue.getBook().getBookTitle());
        dto.setIssueDate(issue.getIssueDate());
        dto.setDueDate(issue.getDueDate());
        dto.setReturnDate(issue.getReturnDate());
        dto.setIssueStatus(issue.getIssueStatus().toString());
        dto.setNotes(issue.getNotes());
        dto.setCreatedAt(issue.getCreatedAt());
        dto.setUpdatedAt(issue.getUpdatedAt());
        return dto;
    }
}
