package com.library.service;

import org.springframework.data.domain.Pageable;
import com.library.dto.CommentDTO;
import com.library.dto.CreateCommentRequest;
import com.library.dto.UpdateCommentRequest;
import com.library.dto.PageResponse;
import java.util.List;

public interface CommentService {
    PageResponse<CommentDTO> getCommentsByPostId(Long postId, Pageable pageable);
    List<CommentDTO> getCommentsByPostId(Long postId);
    CommentDTO getCommentById(Long id);
    CommentDTO createComment(CreateCommentRequest request);
    CommentDTO updateComment(Long id, UpdateCommentRequest request);
    void deleteComment(Long id);
    boolean isOwner(Long commentId, String username);
}
