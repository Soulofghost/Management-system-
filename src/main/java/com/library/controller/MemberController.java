package com.library.controller;

import com.library.dto.MemberDTO;
import com.library.service.MemberService;
import com.library.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<?> createMember(@RequestBody MemberDTO memberDTO, @RequestHeader("Authorization") String token) {
        try {
            String jwt = token.replace("Bearer ", "");
            Integer userId = jwtTokenProvider.getUserIdFromToken(jwt);
            MemberDTO created = memberService.createMember(memberDTO, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMember(@PathVariable Integer id, @RequestBody MemberDTO memberDTO, @RequestHeader("Authorization") String token) {
        try {
            String jwt = token.replace("Bearer ", "");
            Integer userId = jwtTokenProvider.getUserIdFromToken(jwt);
            MemberDTO updated = memberService.updateMember(id, memberDTO, userId);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable Integer id) {
        try {
            memberService.deleteMember(id);
            return ResponseEntity.ok("Member deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMemberById(@PathVariable Integer id) {
        try {
            MemberDTO member = memberService.getMemberById(id);
            return ResponseEntity.ok(member);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllMembers() {
        try {
            List<MemberDTO> members = memberService.getAllMembers();
            return ResponseEntity.ok(members);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/active/all")
    public ResponseEntity<?> getActiveMembers() {
        try {
            List<MemberDTO> members = memberService.getActiveMembers();
            return ResponseEntity.ok(members);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
