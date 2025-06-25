package com.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCategoryRequest {
    @NotNull(message = "Category ID is required")
    Long id;

    @NotBlank(message = "Category code is required")
    String categoryCode;

    @NotBlank(message = "Category name is required")
    String categoryName;

    String description;

    Boolean isActive;
} 