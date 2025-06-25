package com.library.service.impl;

import com.library.dto.StatisticsDTO;
import com.library.repository.BookRepository;
import com.library.repository.PostRepository;
import com.library.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final BookRepository bookRepository;
    private final PostRepository postRepository;

    @Override
    public List<StatisticsDTO.BookByCategory> getBooksByCategory() {
        return bookRepository.countBooksByCategory().stream()
            .map(result -> StatisticsDTO.BookByCategory.builder()
                .categoryName(result.getCategoryName())
                .bookCount(result.getBookCount())
                .build())
            .collect(Collectors.toList());
    }

    @Override
    public List<StatisticsDTO.TopLikedPost> getTopLikedPosts() {
        Pageable pageable = PageRequest.of(0, 5);
        return postRepository.findTop5ByOrderByLikeCountDesc(pageable).stream()
            .map(post -> StatisticsDTO.TopLikedPost.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .author(post.getAuthor().getUsername())
                .likeCount(post.getLikeCount())
                .build())
            .collect(Collectors.toList());
    }
} 