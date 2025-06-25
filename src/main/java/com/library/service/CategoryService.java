package com.library.service;

import com.library.dto.CategoryDTO;
import com.library.dto.CreateCategoryRequest;
import com.library.dto.PageResponse;
import com.library.dto.UpdateCategoryRequest;
import com.library.dto.BookDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    CategoryDTO createCategory(CreateCategoryRequest request);
    CategoryDTO updateCategory(Long id, UpdateCategoryRequest request);
    void deleteCategory(List<Long> ids);
    CategoryDTO getCategoryById(Long id);
    PageResponse<CategoryDTO> searchCategories(String keyword, Boolean isActive, Pageable pageable);
    PageResponse<BookDTO> getBooksByCategoryId(Long categoryId, Pageable pageable);
} 