package com.library.controller;

import com.library.common.ApiResponse;
import com.library.dto.BookDTO;
import com.library.dto.CategoryDTO;
import com.library.dto.CreateCategoryRequest;
import com.library.dto.PageResponse;
import com.library.dto.UpdateCategoryRequest;
import com.library.service.CategoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'USER')")
    public ResponseEntity<PageResponse<CategoryDTO>> searchCategories(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean isActive,
            @PageableDefault(sort = "categoryName", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(categoryService.searchCategories(keyword, isActive, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'USER')")
    public ResponseEntity<ApiResponse<CategoryDTO>> getCategoryById(@PathVariable Long id) {
        CategoryDTO category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(ApiResponse.success(category));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategoryDTO>> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        CategoryDTO createdCategory = categoryService.createCategory(request);
        return ResponseEntity.ok(ApiResponse.created(createdCategory));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategoryDTO>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequest request) {
        CategoryDTO updatedCategory = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(ApiResponse.success(updatedCategory));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@RequestParam List<Long> ids) {
        categoryService.deleteCategory(ids);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/{categoryId}/books")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'USER')")
    public ResponseEntity<PageResponse<BookDTO>> getBooksByCategoryId(
            @PathVariable Long categoryId,
            @PageableDefault(sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(categoryService.getBooksByCategoryId(categoryId, pageable));
    }
} 