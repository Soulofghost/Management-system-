package com.library.controller;

import com.library.dto.PublisherDTO;
import com.library.service.PublisherService;
import com.library.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publishers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PublisherController {

    private final PublisherService publisherService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<?> createPublisher(@RequestBody PublisherDTO publisherDTO, @RequestHeader("Authorization") String token) {
        try {
            String jwt = token.replace("Bearer ", "");
            Integer userId = jwtTokenProvider.getUserIdFromToken(jwt);
            PublisherDTO created = publisherService.createPublisher(publisherDTO, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePublisher(@PathVariable Integer id, @RequestBody PublisherDTO publisherDTO, @RequestHeader("Authorization") String token) {
        try {
            String jwt = token.replace("Bearer ", "");
            Integer userId = jwtTokenProvider.getUserIdFromToken(jwt);
            PublisherDTO updated = publisherService.updatePublisher(id, publisherDTO, userId);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePublisher(@PathVariable Integer id) {
        try {
            publisherService.deletePublisher(id);
            return ResponseEntity.ok("Publisher deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPublisherById(@PathVariable Integer id) {
        try {
            PublisherDTO publisher = publisherService.getPublisherById(id);
            return ResponseEntity.ok(publisher);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllPublishers() {
        try {
            List<PublisherDTO> publishers = publisherService.getAllPublishers();
            return ResponseEntity.ok(publishers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/active/all")
    public ResponseEntity<?> getActivePublishers() {
        try {
            List<PublisherDTO> publishers = publisherService.getActivePublishers();
            return ResponseEntity.ok(publishers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
