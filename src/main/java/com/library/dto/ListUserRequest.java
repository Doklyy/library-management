package com.library.dto;

import lombok.Data;

@Data
public class ListUserRequest {
    private String search;
    private String sortBy;
    private String sortDirection;
} 