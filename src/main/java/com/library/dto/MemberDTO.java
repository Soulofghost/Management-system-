package com.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private Integer memberId;
    private String memberCode;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private LocalDate dateOfBirth;
    private String gender;
    private LocalDate membershipDate;
    private String membershipType;
    private LocalDate expiryDate;
    private Boolean isActive;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
