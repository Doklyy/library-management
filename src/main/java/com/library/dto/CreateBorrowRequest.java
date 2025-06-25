package com.library.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateBorrowRequest {
    private Long userId;
    private List<BookQuantityRequest> books;
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;

    @Data
    public static class BookQuantityRequest {
        private Long bookId;
        private Integer quantity;
    }
} 