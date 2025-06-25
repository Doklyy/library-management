package com.library.controller;

import com.library.dto.StatisticsDTO;
import com.library.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping("/books-by-category")
    public ResponseEntity<List<StatisticsDTO.BookByCategory>> getBooksByCategory() {
        return ResponseEntity.ok(statisticsService.getBooksByCategory());
    }

    @GetMapping("/top-liked-posts")
    public ResponseEntity<List<StatisticsDTO.TopLikedPost>> getTopLikedPosts() {
        return ResponseEntity.ok(statisticsService.getTopLikedPosts());
    }
} 