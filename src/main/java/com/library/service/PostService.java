package com.library.service;

import com.library.dto.PostDTO;
import com.library.dto.CreatePostRequest;
import com.library.dto.UpdatePostRequest;
import com.library.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface PostService {
    PageResponse<PostDTO> searchPosts(String keyword, Boolean isActive, Pageable pageable);
    PostDTO getPostById(Long id);
    PostDTO createPost(CreatePostRequest request);
    PostDTO updatePost(Long id, UpdatePostRequest request);
    void deletePost(Long id);
    boolean isOwner(Long postId, String username);
    List<Map<String, Object>> getTopLikedPosts(int limit);
} 