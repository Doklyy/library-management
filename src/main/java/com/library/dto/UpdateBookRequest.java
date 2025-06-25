package com.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateBookRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @NotBlank(message = "Publisher is required")
    private String publisher;

    @NotBlank(message = "Language is required")
    private String language;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Publication year is required")
    private Integer publicationYear;

    @NotNull(message = "Page count is required")
    @Min(value = 1, message = "Page count must be at least 1")
    private Integer pageCount;

    @NotBlank(message = "ISBN is required")
    private String isbn;

    @NotNull(message = "Publication date is required")
    private LocalDate publicationDate;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    private String coverImage;
    private String format;
    private String condition;
    private String location;
    private List<String> tags;
    private Boolean isActive;
    private Boolean isDeleted;
}
