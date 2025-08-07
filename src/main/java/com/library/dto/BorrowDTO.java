package com.library.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BorrowDTO {
    private Long id;
    private Long userId;
    private List<Long> bookIds;
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private String status;
    private String bookborrowed;
} 