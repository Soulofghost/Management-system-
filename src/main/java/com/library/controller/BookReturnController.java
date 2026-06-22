package com.library.controller;

import com.library.dto.BookReturnDTO;
import com.library.service.BookReturnService;
import com.library.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/returns")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BookReturnController {

    private final BookReturnService bookReturnService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<?> returnBook(@RequestBody BookReturnDTO bookReturnDTO, @RequestHeader("Authorization") String token) {
        try {
            String jwt = token.replace("Bearer ", "");
            Integer userId = jwtTokenProvider.getUserIdFromToken(jwt);
            BookReturnDTO created = bookReturnService.returnBook(bookReturnDTO, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReturnById(@PathVariable Integer id) {
        try {
            BookReturnDTO bookReturn = bookReturnService.getReturnById(id);
            return ResponseEntity.ok(bookReturn);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/issue/{issueId}")
    public ResponseEntity<?> getReturnByIssueId(@PathVariable Integer issueId) {
        try {
            BookReturnDTO bookReturn = bookReturnService.getReturnByIssueId(issueId);
            return ResponseEntity.ok(bookReturn);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/overdue/all")
    public ResponseEntity<?> getOverdueBooks() {
        try {
            List<BookReturnDTO> overdueBooks = bookReturnService.getOverdueBooks();
            return ResponseEntity.ok(overdueBooks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/unpaid-fines/all")
    public ResponseEntity<?> getUnpaidFines() {
        try {
            List<BookReturnDTO> unpaidFines = bookReturnService.getUnpaidFines();
            return ResponseEntity.ok(unpaidFines);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/pay-fine")
    public ResponseEntity<?> markFineAsPaid(@PathVariable Integer id, @RequestParam String paymentMethod) {
        try {
            BookReturnDTO bookReturn = bookReturnService.markFineAsPaid(id, paymentMethod);
            return ResponseEntity.ok(bookReturn);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}
