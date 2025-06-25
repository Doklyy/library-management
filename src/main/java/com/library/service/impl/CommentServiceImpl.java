package com.library.service.impl;

import com.library.dto.CommentDTO;
import com.library.dto.CreateCommentRequest;
import com.library.dto.UpdateCommentRequest;
import com.library.dto.PageResponse;
import com.library.entity.Comment;
import com.library.entity.User;
import com.library.repository.CommentRepository;
import com.library.repository.PostRepository;
import com.library.repository.UserRepository;
import com.library.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, ModelMapper modelMapper, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PageResponse<CommentDTO> getCommentsByPostId(Long postId, Pageable pageable) {
        Page<Comment> commentsPage = commentRepository.findByPostId(postId, pageable);
        List<CommentDTO> commentDTOs = commentsPage.getContent().stream()
                .map(comment -> modelMapper.map(comment, CommentDTO.class))
                .collect(Collectors.toList());

        return new PageResponse<>(
                commentDTOs,
                commentsPage.getNumber(),
                commentsPage.getSize(),
                commentsPage.getTotalElements(),
                commentsPage.getTotalPages(),
                commentsPage.isLast()
        );
    }

    @Override
    public List<CommentDTO> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(comment -> modelMapper.map(comment, CommentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CommentDTO getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
        return modelMapper.map(comment, CommentDTO.class);
    }

    @Override
    public CommentDTO createComment(CreateCommentRequest request) {
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setPost(postRepository.findById(request.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found")));
        User author = userRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        comment.setAuthor(author);
        comment.setUser(author);
        Comment savedComment = commentRepository.save(comment);
        return modelMapper.map(savedComment, CommentDTO.class);
    }

    @Override
    public CommentDTO updateComment(Long id, UpdateCommentRequest request) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
        existingComment.setContent(request.getContent());
        Comment updatedComment = commentRepository.save(existingComment);
        return modelMapper.map(updatedComment, CommentDTO.class);
    }

    @Override
    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new RuntimeException("Comment not found with id: " + id);
        }
        commentRepository.deleteById(id);
    }

    @Override
    public boolean isOwner(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + commentId));
        return comment.getUser().getUsername().equals(username);
    }
} 