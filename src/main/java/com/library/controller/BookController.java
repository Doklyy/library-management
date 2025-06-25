package com.library.controller;

import com.library.common.ApiResponse;
import com.library.dto.PageResponse;
import com.library.dto.BookDTO;
import com.library.dto.BookSearchRequest;
import com.library.dto.CreateBookRequest;
import com.library.dto.UpdateBookRequest;
import com.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public ResponseEntity<PageResponse<BookDTO>> searchBooks(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean available,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        BookSearchRequest request = new BookSearchRequest();
        request.setKeyword(keyword);
        request.setCategoryId(category != null ? Long.parseLong(category) : null);
        request.setIsActive(available);
        return ResponseEntity.ok(bookService.searchBooks(request, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody CreateBookRequest request) {
        return ResponseEntity.ok(bookService.createBook(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDTO> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBookRequest request) {
        return ResponseEntity.ok(bookService.updateBook(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(List.of(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.ok(bookService.getAllCategories());
    }

    @GetMapping("/statistics/top-borrowed")
    public ResponseEntity<?> getTopBorrowedBooks(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(bookService.getTopBorrowedBooks(limit));
    }

    @GetMapping("/statistics/category")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getBookCategoryStatistics() {
        List<Map<String, Object>> statistics = bookService.getBookCategoryStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics));
    }
}