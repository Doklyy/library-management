package com.library.controller;

import com.library.entity.Book;
import com.library.service.GoogleBooksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/google-books")
public class GoogleBooksController {

    private final GoogleBooksService googleBooksService;

    @Autowired
    public GoogleBooksController(GoogleBooksService googleBooksService) {
        this.googleBooksService = googleBooksService;
    }

    @GetMapping("/sync")
    public ResponseEntity<List<Book>> syncBooks(@RequestParam String query) {
        log.info("Starting to sync books with query: {}", query);
        List<Book> syncedBooks = googleBooksService.syncBooks(query);
        log.info("Successfully synced {} books", syncedBooks.size());
        return ResponseEntity.ok(syncedBooks);
    }
} 