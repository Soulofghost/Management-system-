package com.library.service;

import com.library.dto.MemberDTO;
import com.library.entity.Member;
import com.library.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    public MemberDTO createMember(MemberDTO memberDTO, Integer userId) {
        if (memberRepository.findByMemberCode(memberDTO.getMemberCode()).isPresent()) {
            throw new RuntimeException("Member code already exists");
        }

        if (memberRepository.findByEmail(memberDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        Member member = modelMapper.map(memberDTO, Member.class);
        member.setCreatedBy(userId);
        member.setIsActive(true);
        member.setCreatedAt(LocalDateTime.now());
        member.setUpdatedAt(LocalDateTime.now());

        Member savedMember = memberRepository.save(member);
        return modelMapper.map(savedMember, MemberDTO.class);
    }

    public MemberDTO updateMember(Integer memberId, MemberDTO memberDTO, Integer userId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("Member not found"));

        member.setFirstName(memberDTO.getFirstName());
        member.setLastName(memberDTO.getLastName());
        member.setPhone(memberDTO.getPhone());
        member.setAddress(memberDTO.getAddress());
        member.setCity(memberDTO.getCity());
        member.setState(memberDTO.getState());
        member.setCountry(memberDTO.getCountry());
        member.setPostalCode(memberDTO.getPostalCode());
        member.setDateOfBirth(memberDTO.getDateOfBirth());
        member.setGender(memberDTO.getGender());
        member.setMembershipType(memberDTO.getMembershipType());
        member.setExpiryDate(memberDTO.getExpiryDate());
        member.setNotes(memberDTO.getNotes());
        member.setUpdatedAt(LocalDateTime.now());

        Member updatedMember = memberRepository.save(member);
        return modelMapper.map(updatedMember, MemberDTO.class);
    }

    public void deleteMember(Integer memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("Member not found"));
        memberRepository.delete(member);
    }

    public MemberDTO getMemberById(Integer memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("Member not found"));
        return modelMapper.map(member, MemberDTO.class);
    }

    public List<MemberDTO> getAllMembers() {
        return memberRepository.findAllByOrderByFirstNameAsc()
            .stream()
            .map(member -> modelMapper.map(member, MemberDTO.class))
            .collect(Collectors.toList());
    }

    public List<MemberDTO> getActiveMembers() {
        return memberRepository.findAllByIsActiveTrueOrderByFirstNameAsc()
            .stream()
            .map(member -> modelMapper.map(member, MemberDTO.class))
            .collect(Collectors.toList());
    }
}
