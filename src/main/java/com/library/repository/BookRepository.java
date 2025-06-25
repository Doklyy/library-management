package com.library.repository;

import com.library.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    @Query("SELECT DISTINCT b FROM Book b LEFT JOIN b.categories c WHERE " +
           "(:keyword IS NULL OR (b.title LIKE %:keyword% OR b.author LIKE %:keyword%)) AND " +
           "(:categoryId IS NULL OR c.id = :categoryId) AND " +
           "(:isActive IS NULL OR b.isActive = :isActive) AND " +
           "b.isDeleted = false")
    Page<Book> searchBooks(@Param("keyword") String keyword,
                          @Param("categoryId") Long categoryId,
                          @Param("isActive") Boolean isActive,
                          Pageable pageable);

    Page<Book> findByCategories_Id(Long categoryId, Pageable pageable);

    @Query("SELECT c.categoryName as categoryName, COUNT(b) as bookCount " +
           "FROM Book b " +
           "JOIN b.categories c " +
           "WHERE b.isDeleted = false " +
           "GROUP BY c.categoryName " +
           "ORDER BY bookCount DESC")
    List<BookCategoryCount> countBooksByCategory();

    @Query("SELECT b FROM Book b JOIN b.categories c WHERE c.id IN :categoryIds AND b.isDeleted = false")
    List<Book> findByCategoryIds(@Param("categoryIds") List<Long> categoryIds);

    @Query("SELECT b FROM Book b JOIN b.borrowReceipts br WHERE b.isDeleted = false " +
           "GROUP BY b ORDER BY COUNT(br) DESC")
    List<Book> findTopBorrowedBooks(Pageable pageable);

    interface BookCategoryCount {
        String getCategoryName();
        Long getBookCount();
    }
}