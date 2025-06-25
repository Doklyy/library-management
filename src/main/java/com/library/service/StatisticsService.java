package com.library.service;

import com.library.dto.StatisticsDTO;
import java.util.List;

public interface StatisticsService {
    List<StatisticsDTO.BookByCategory> getBooksByCategory();
    List<StatisticsDTO.TopLikedPost> getTopLikedPosts();
} 