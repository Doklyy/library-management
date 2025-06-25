package com.library.repository;

import com.library.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCategoryName(String categoryName);
    boolean existsByCategoryName(String categoryName);
    Optional<Category> findByCategoryCode(String categoryCode);

    @Query("SELECT c FROM Category c WHERE " +
           "(:keyword IS NULL OR c.categoryName LIKE %:keyword% OR c.categoryCode LIKE %:keyword%) AND " +
           "(:isActive IS NULL OR c.isActive = :isActive) AND " +
           "c.isDeleted = false")
    Page<Category> searchCategories(
        @Param("keyword") String keyword,
        @Param("isActive") Boolean isActive,
        Pageable pageable
    );
} 