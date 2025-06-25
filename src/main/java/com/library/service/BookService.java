package com.library.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.library.dto.BookDTO;
import com.library.dto.BookSearchRequest;
import com.library.dto.CreateBookRequest;
import com.library.dto.PageResponse;
import com.library.dto.UpdateBookRequest;

public interface BookService {
    BookDTO createBook(CreateBookRequest request);
    BookDTO updateBook(Long id, UpdateBookRequest request);
    void deleteBook(List<Long> ids);
    BookDTO getBookById(Long id);
    PageResponse<BookDTO> searchBooks(BookSearchRequest request, Pageable pageable);
    void syncBooksFromGoogleBooks();
    List<Map<String, Object>> getBookCategoryStatistics();
    List<BookDTO> getBooksByCategoryIds(List<Long> categoryIds);
    List<Map<String, Object>> getAllCategories();
    List<BookDTO> getTopBorrowedBooks(int limit);
}