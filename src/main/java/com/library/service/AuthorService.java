package com.library.service;

import com.library.dto.AuthorDTO;
import com.library.entity.Author;
import com.library.repository.AuthorRepository;
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
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final ModelMapper modelMapper;

    public AuthorDTO createAuthor(AuthorDTO authorDTO, Integer userId) {
        Author author = modelMapper.map(authorDTO, Author.class);
        author.setCreatedBy(userId);
        author.setIsActive(true);
        author.setCreatedAt(LocalDateTime.now());
        author.setUpdatedAt(LocalDateTime.now());

        Author savedAuthor = authorRepository.save(author);
        return modelMapper.map(savedAuthor, AuthorDTO.class);
    }

    public AuthorDTO updateAuthor(Integer authorId, AuthorDTO authorDTO, Integer userId) {
        Author author = authorRepository.findById(authorId)
            .orElseThrow(() -> new RuntimeException("Author not found"));

        author.setAuthorName(authorDTO.getAuthorName());
        author.setEmail(authorDTO.getEmail());
        author.setPhone(authorDTO.getPhone());
        author.setAddress(authorDTO.getAddress());
        author.setCity(authorDTO.getCity());
        author.setState(authorDTO.getState());
        author.setCountry(authorDTO.getCountry());
        author.setPostalCode(authorDTO.getPostalCode());
        author.setBiography(authorDTO.getBiography());
        author.setDateOfBirth(authorDTO.getDateOfBirth());
        author.setUpdatedAt(LocalDateTime.now());

        Author updatedAuthor = authorRepository.save(author);
        return modelMapper.map(updatedAuthor, AuthorDTO.class);
    }

    public void deleteAuthor(Integer authorId) {
        Author author = authorRepository.findById(authorId)
            .orElseThrow(() -> new RuntimeException("Author not found"));
        authorRepository.delete(author);
    }

    public AuthorDTO getAuthorById(Integer authorId) {
        Author author = authorRepository.findById(authorId)
            .orElseThrow(() -> new RuntimeException("Author not found"));
        return modelMapper.map(author, AuthorDTO.class);
    }

    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAllByOrderByAuthorNameAsc()
            .stream()
            .map(author -> modelMapper.map(author, AuthorDTO.class))
            .collect(Collectors.toList());
    }

    public List<AuthorDTO> getActiveAuthors() {
        return authorRepository.findAllByIsActiveTrueOrderByAuthorNameAsc()
            .stream()
            .map(author -> modelMapper.map(author, AuthorDTO.class))
            .collect(Collectors.toList());
    }
}
