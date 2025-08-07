package com.library.service.impl;

import com.library.dto.BookDTO;
import com.library.dto.BookSearchRequest;
import com.library.dto.CreateBookRequest;
import com.library.dto.UpdateBookRequest;
import com.library.dto.PageResponse;
import com.library.entity.Book;
import com.library.entity.Category;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.BookRepository;
import com.library.repository.CategoryRepository;
import com.library.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    // Logic constants
    private static final boolean DEFAULT_ACTIVE = true;
    private static final boolean DEFAULT_DELETED = false;
    private static final int DEFAULT_PUBLICATION_YEAR = 2024;

    public BookServiceImpl(BookRepository bookRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public BookDTO createBook(CreateBookRequest request) {
        log.info("Creating new book with request: {}", request);
        
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPublisher(request.getPublisher());
        book.setPublicationYear(request.getPublicationYear() != null ? request.getPublicationYear() : DEFAULT_PUBLICATION_YEAR);
        book.setDescription(request.getDescription());
        book.setLanguage(request.getLanguage());
        book.setPageCount(request.getPageCount());
        book.setActive(DEFAULT_ACTIVE);
        book.setDeleted(DEFAULT_DELETED);
        book.setCreatedAt(LocalDateTime.now());
        book.setCreatedBy("SYSTEM");
        book.setQuantity(request.getQuantity());
        book.setAvailable(request.getQuantity());
        book.setBorrowed(0);

        // Set categories
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            Set<Category> categories = request.getCategoryIds().stream()
                .map(categoryId -> categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId)))
                .collect(Collectors.toSet());
            book.setCategories(categories);
        }

        Book savedBook = bookRepository.save(book);
        log.info("Successfully created book with id: {}", savedBook.getId());
        
        return modelMapper.map(savedBook, BookDTO.class);
    }

    @Override
    public BookDTO updateBook(Long id, UpdateBookRequest request) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        
        // Map updated fields from request to book entity
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setDescription(request.getDescription());
        book.setPublisher(request.getPublisher());
        book.setLanguage(request.getLanguage());
        
        if (request.getIsActive() != null) {
            book.setActive(request.getIsActive());
        }
        if (request.getIsDeleted() != null) {
            book.setDeleted(request.getIsDeleted());
        }
        book.setUpdatedAt(LocalDateTime.now());
        book.setUpdatedBy("SYSTEM");

        // Set category
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
            book.setCategories(Set.of(category));
        }
        
        Book updatedBook = bookRepository.save(book);
        return modelMapper.map(updatedBook, BookDTO.class);
    }

    @Override
    public void deleteBook(List<Long> ids) {
        bookRepository.deleteAllById(ids);
    }

    @Override
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        return modelMapper.map(book, BookDTO.class);
    }

    @Override
    public PageResponse<BookDTO> searchBooks(BookSearchRequest request, Pageable pageable) {
        Page<Book> bookPage = bookRepository.searchBooks(
            request.getKeyword(),
            request.getCategoryId(),
            request.getAvailable(),
            pageable
        );
        
        List<BookDTO> bookDTOs = bookPage.getContent().stream()
            .map(book -> modelMapper.map(book, BookDTO.class))
            .collect(Collectors.toList());
            
        return new PageResponse<>(
            bookDTOs,
            bookPage.getNumber(),
            bookPage.getSize(),
            bookPage.getTotalElements(),
            bookPage.getTotalPages(),
            bookPage.isLast()
        );
    }

    @Override
    public void syncBooksFromGoogleBooks() {
        // TODO: Implement Google Books API integration
        throw new UnsupportedOperationException("Google Books sync not implemented yet");
    }

    @Override
    public List<Map<String, Object>> getBookCategoryStatistics() {
        return bookRepository.countBooksByCategory().stream()
                .map(count -> {
                    Map<String, Object> map = Map.of(
                        "categoryName", count.getCategoryName(),
                        "bookCount", count.getBookCount()
                    );
                    return map;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> getBooksByCategoryIds(List<Long> categoryIds) {
        List<Book> books = bookRepository.findByCategoryIds(categoryIds);
        return books.stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> {
                    Map<String, Object> map = Map.of(
                        "id", category.getId(),
                        "name", category.getCategoryName(),
                        "description", category.getDescription()
                    );
                    return map;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> getTopBorrowedBooks(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Book> books = bookRepository.findTopBorrowedBooks(pageable);
        return books.stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .collect(Collectors.toList());
    }
}