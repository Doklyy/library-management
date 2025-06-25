package com.library.dto;

import lombok.Data;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Data
public class CreateCommentRequest {
    @NotNull
    private Long postId;
    
    @NotNull
    private Long authorId;
    
    @NotEmpty
    private String content;
}
