package com.library.controller;

import com.library.dto.BookIssueDTO;
import com.library.service.BookIssueService;
import com.library.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BookIssueController {

    private final BookIssueService bookIssueService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<?> issueBook(@RequestBody BookIssueDTO bookIssueDTO, @RequestHeader("Authorization") String token) {
        try {
            String jwt = token.replace("Bearer ", "");
            Integer userId = jwtTokenProvider.getUserIdFromToken(jwt);
            BookIssueDTO created = bookIssueService.issueBook(bookIssueDTO, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getIssueById(@PathVariable Integer id) {
        try {
            BookIssueDTO issue = bookIssueService.getIssueById(id);
            return ResponseEntity.ok(issue);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/active/all")
    public ResponseEntity<?> getAllActiveIssues() {
        try {
            List<BookIssueDTO> issues = bookIssueService.getAllActiveIssues();
            return ResponseEntity.ok(issues);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<?> getMemberIssues(@PathVariable Integer memberId) {
        try {
            List<BookIssueDTO> issues = bookIssueService.getMemberIssues(memberId);
            return ResponseEntity.ok(issues);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
