package com.library.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.library.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE " +
           "(:keyword IS NULL OR (p.title LIKE %:keyword% OR p.content LIKE %:keyword%)) AND " +
           "(:isActive IS NULL OR p.isActive = :isActive) AND " +
           "p.isDeleted = false")
    Page<Post> searchPosts(@Param("keyword") String keyword,
                          @Param("isActive") Boolean isActive,
                          Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.isDeleted = false ORDER BY p.likeCount DESC")
    List<Post> findTopLikedPosts(Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.isDeleted = false ORDER BY p.likeCount DESC")
    List<Post> findTop5ByOrderByLikeCountDesc(Pageable pageable);
} 