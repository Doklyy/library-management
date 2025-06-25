package com.library.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserSearchRequest {
    private String keyword;
    private Boolean isActive;
    private String role;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
}
