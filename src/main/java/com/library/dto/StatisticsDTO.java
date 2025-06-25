package com.library.dto;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class StatisticsDTO {
    @Data
    @Builder
    public static class BookByCategory {
        private String categoryName;
        private Long bookCount;
    }

    @Data
    @Builder
    public static class TopLikedPost {
        private Long postId;
        private String title;
        private String author;
        private Long likeCount;
    }
} 