package com.library.service;

import com.library.dto.BookDTO;
import com.library.entity.Book;
import com.library.entity.Author;
import com.library.entity.Publisher;
import com.library.entity.Category;
import com.library.repository.BookRepository;
import com.library.repository.AuthorRepository;
import com.library.repository.PublisherRepository;
import com.library.repository.CategoryRepository;
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
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public BookDTO createBook(BookDTO bookDTO, Integer userId) {
        Author author = authorRepository.findById(bookDTO.getAuthorId())
            .orElseThrow(() -> new RuntimeException("Author not found"));
        
        Publisher publisher = publisherRepository.findById(bookDTO.getPublisherId())
            .orElseThrow(() -> new RuntimeException("Publisher not found"));
        
        Category category = categoryRepository.findById(bookDTO.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Category not found"));

        Book book = new Book();
        book.setIsbn(bookDTO.getIsbn());
        book.setBookTitle(bookDTO.getBookTitle());
        book.setAuthor(author);
        book.setPublisher(publisher);
        book.setCategory(category);
        book.setEdition(bookDTO.getEdition());
        book.setPublicationDate(bookDTO.getPublicationDate());
        book.setTotalCopies(bookDTO.getTotalCopies());
        book.setAvailableCopies(bookDTO.getAvailableCopies());
        book.setBookPrice(bookDTO.getBookPrice());
        book.setDescription(bookDTO.getDescription());
        book.setAcquisitionDate(bookDTO.getAcquisitionDate());
        book.setShelfLocation(bookDTO.getShelfLocation());
        book.setIsActive(true);
        book.setCreatedBy(userId);
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());

        Book savedBook = bookRepository.save(book);
        return mapToDTO(savedBook);
    }

    public BookDTO updateBook(Integer bookId, BookDTO bookDTO, Integer userId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new RuntimeException("Book not found"));

        if (bookDTO.getAuthorId() != null) {
            Author author = authorRepository.findById(bookDTO.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found"));
            book.setAuthor(author);
        }

        if (bookDTO.getPublisherId() != null) {
            Publisher publisher = publisherRepository.findById(bookDTO.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found"));
            book.setPublisher(publisher);
        }

        if (bookDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(bookDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
            book.setCategory(category);
        }

        book.setBookTitle(bookDTO.getBookTitle());
        book.setEdition(bookDTO.getEdition());
        book.setPublicationDate(bookDTO.getPublicationDate());
        book.setTotalCopies(bookDTO.getTotalCopies());
        book.setAvailableCopies(bookDTO.getAvailableCopies());
        book.setBookPrice(bookDTO.getBookPrice());
        book.setDescription(bookDTO.getDescription());
        book.setShelfLocation(bookDTO.getShelfLocation());
        book.setUpdatedAt(LocalDateTime.now());

        Book updatedBook = bookRepository.save(book);
        return mapToDTO(updatedBook);
    }

    public void deleteBook(Integer bookId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new RuntimeException("Book not found"));
        bookRepository.delete(book);
    }

    public BookDTO getBookById(Integer bookId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new RuntimeException("Book not found"));
        return mapToDTO(book);
    }

    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll()
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public List<BookDTO> getActiveBooks() {
        return bookRepository.findAllByIsActiveTrueOrderByBookTitleAsc()
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    private BookDTO mapToDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setBookId(book.getBookId());
        dto.setIsbn(book.getIsbn());
        dto.setBookTitle(book.getBookTitle());
        dto.setAuthorId(book.getAuthor().getAuthorId());
        dto.setAuthorName(book.getAuthor().getAuthorName());
        dto.setPublisherId(book.getPublisher().getPublisherId());
        dto.setPublisherName(book.getPublisher().getPublisherName());
        dto.setCategoryId(book.getCategory().getCategoryId());
        dto.setCategoryName(book.getCategory().getCategoryName());
        dto.setEdition(book.getEdition());
        dto.setPublicationDate(book.getPublicationDate());
        dto.setTotalCopies(book.getTotalCopies());
        dto.setAvailableCopies(book.getAvailableCopies());
        dto.setBookPrice(book.getBookPrice());
        dto.setDescription(book.getDescription());
        dto.setAcquisitionDate(book.getAcquisitionDate());
        dto.setShelfLocation(book.getShelfLocation());
        dto.setIsActive(book.getIsActive());
        dto.setCreatedAt(book.getCreatedAt());
        dto.setUpdatedAt(book.getUpdatedAt());
        return dto;
    }
}
