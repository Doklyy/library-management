package com.library.controller;

import com.library.dto.ApiResponse;
import com.library.dto.CommentDTO;
import com.library.dto.CreateCommentRequest;
import com.library.dto.UpdateCommentRequest;
import com.library.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/post/{postId}")
    public ResponseEntity<ApiResponse<List<CommentDTO>>> getCommentsByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(ApiResponse.success(commentService.getCommentsByPostId(postId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentDTO>> getCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(commentService.getCommentById(id)));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CommentDTO>> createComment(@Valid @RequestBody CreateCommentRequest request) {
        return ResponseEntity.ok(ApiResponse.success(commentService.createComment(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated() and @commentService.isOwner(#id, authentication.principal.username)")
    public ResponseEntity<ApiResponse<CommentDTO>> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCommentRequest request) {
        return ResponseEntity.ok(ApiResponse.success(commentService.updateComment(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated() and @commentService.isOwner(#id, authentication.principal.username)")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
