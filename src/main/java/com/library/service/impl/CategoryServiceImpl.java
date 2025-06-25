package com.library.service.impl;

import com.library.dto.CategoryDTO;
import com.library.dto.CreateCategoryRequest;
import com.library.dto.PageResponse;
import com.library.dto.UpdateCategoryRequest;
import com.library.entity.Category;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.CategoryRepository;
import com.library.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.library.entity.Book;
import com.library.dto.BookDTO;
import com.library.repository.BookRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final BookRepository bookRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public CategoryDTO createCategory(CreateCategoryRequest request) {
        // Validate before transaction
        if (categoryRepository.existsByCategoryName(request.getCategoryName())) {
            throw new IllegalArgumentException("Category with name " + request.getCategoryName() + " already exists");
        }
        if (categoryRepository.findByCategoryCode(request.getCategoryCode()).isPresent()) {
            throw new IllegalArgumentException("Category with code " + request.getCategoryCode() + " already exists");
        }

        // Create and save category
        Category category = new Category();
        category.setCategoryCode(request.getCategoryCode());
        category.setCategoryName(request.getCategoryName());
        category.setDescription(request.getDescription());
        category.setActive(true);
        category.setDeleted(false);
        
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public CategoryDTO updateCategory(Long id, UpdateCategoryRequest request) {
        // Find category
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        // Validate before update
        if (!category.getCategoryName().equals(request.getCategoryName()) && 
            categoryRepository.existsByCategoryName(request.getCategoryName())) {
            throw new IllegalArgumentException("Category with name " + request.getCategoryName() + " already exists");
        }
        if (!category.getCategoryCode().equals(request.getCategoryCode()) && 
            categoryRepository.findByCategoryCode(request.getCategoryCode()).isPresent()) {
            throw new IllegalArgumentException("Category with code " + request.getCategoryCode() + " already exists");
        }

        // Update category
        category.setCategoryCode(request.getCategoryCode());
        category.setCategoryName(request.getCategoryName());
        category.setDescription(request.getDescription());
        if (request.getIsActive() != null) {
            category.setActive(request.getIsActive());
        }
        
        Category updatedCategory = categoryRepository.save(category);
        return modelMapper.map(updatedCategory, CategoryDTO.class);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCategory(List<Long> ids) {
        categoryRepository.deleteAllById(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CategoryDTO> searchCategories(String keyword, Boolean isActive, Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.searchCategories(keyword, isActive, pageable);
        List<CategoryDTO> categoryDTOs = categoryPage.getContent().stream()
            .map(category -> modelMapper.map(category, CategoryDTO.class))
            .collect(Collectors.toList());

        return new PageResponse<>(
            categoryDTOs,
            categoryPage.getNumber(),
            categoryPage.getSize(),
            categoryPage.getTotalElements(),
            categoryPage.getTotalPages(),
            categoryPage.isLast()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<BookDTO> getBooksByCategoryId(Long categoryId, Pageable pageable) {
        Page<Book> bookPage = bookRepository.findByCategories_Id(categoryId, pageable);
        List<BookDTO> bookDTOs = bookPage.getContent().stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .collect(Collectors.toList());

        return new PageResponse<>(
                bookDTOs,
                bookPage.getNumber(),
                bookPage.getSize(),
                bookPage.getTotalElements(),
                bookPage.getTotalPages(),
                bookPage.isLast()
        );
    }
} 