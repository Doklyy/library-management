package com.library.service.impl;

import com.library.entity.Book;
import com.library.entity.Category;
import com.library.repository.BookRepository;
import com.library.repository.CategoryRepository;
import com.library.service.GoogleBooksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class GoogleBooksServiceImpl implements GoogleBooksService {
    private final RestTemplate restTemplate;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    private static final int MAX_DESCRIPTION_LENGTH = 1000;

    @Value("${google.books.api.url:https://www.googleapis.com/books/v1/volumes}")
    private String apiUrl;

    public GoogleBooksServiceImpl(RestTemplate restTemplate, BookRepository bookRepository, CategoryRepository categoryRepository) {
        this.restTemplate = restTemplate;
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public List<Book> syncBooks(String query) {
        log.info("Starting to sync books with query: {}", query);
        
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("q", query)
                .build()
                .toUriString();
        
        log.info("Calling Google Books API with URL: {}", url);

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        log.info("Response status: {}", response.getStatusCode());
        
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Google Books API returned error status: {}", response.getStatusCode());
            throw new RuntimeException("Google Books API returned error status: " + response.getStatusCode());
        }

        Map<String, Object> responseBody = response.getBody();
        if (responseBody == null) {
            log.error("Response body is null");
            throw new RuntimeException("Response body is null");
        }

        List<Map<String, Object>> items = (List<Map<String, Object>>) responseBody.get("items");
        if (items == null) {
            log.warn("No books found in the response");
            return new ArrayList<>();
        }

        log.info("Found {} books in the response", items.size());
        List<Book> books = new ArrayList<>();
        
        for (Map<String, Object> item : items) {
            Map<String, Object> volumeInfo = (Map<String, Object>) item.get("volumeInfo");
            if (volumeInfo == null) {
                log.warn("Volume info is null for item: {}", item);
                continue;
            }

            Book book = new Book();
            // Set required fields
            book.setTitle((String) volumeInfo.get("title"));
            
            // Handle authors
            List<String> authors = (List<String>) volumeInfo.get("authors");
            if (authors != null && !authors.isEmpty()) {
                book.setAuthor(String.join(", ", authors));
            }
            
            book.setPublisher((String) volumeInfo.get("publisher"));
            
            // Handle pageCount
            Object pageCount = volumeInfo.get("pageCount");
            if (pageCount != null) {
                book.setPageCount((Integer) pageCount);
            }
            
            // Handle categories (printType)
            String printType = (String) volumeInfo.get("printType");
            if (printType != null && !printType.isEmpty()) {
                log.debug("Processing category: {}", printType);
                Category category = categoryRepository.findByCategoryName(printType)
                        .orElseGet(() -> {
                            log.info("Creating new category: {}", printType);
                            Category newCategory = new Category();
                            newCategory.setCategoryName(printType);
                            newCategory.setCategoryCode(printType.toUpperCase().replace(" ", "_"));
                            newCategory.setActive(true);
                            newCategory.setDeleted(false);
                            return categoryRepository.save(newCategory);
                        });
                Set<Category> categories = new HashSet<>();
                categories.add(category);
                book.setCategories(categories);
            }
            
            book.setLanguage((String) volumeInfo.get("language"));
            
            // Handle description with length limit
            String description = (String) volumeInfo.get("description");
            if (description != null && description.length() > MAX_DESCRIPTION_LENGTH) {
                description = description.substring(0, MAX_DESCRIPTION_LENGTH - 3) + "...";
            }
            book.setDescription(description);

            // Set default values for required fields
            book.setPublicationYear(2024);
            book.setActive(true);
            book.setDeleted(false);
            
            books.add(book);
            log.debug("Processed book: {}", book.getTitle());
        }

        if (books.isEmpty()) {
            log.warn("No valid books found to save");
            return new ArrayList<>();
        }

        List<Book> savedBooks = bookRepository.saveAll(books);
        log.info("Successfully saved {} books to the database", savedBooks.size());
        return savedBooks;
    }
} 