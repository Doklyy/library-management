package com.library.service.impl;

import com.library.dto.PostDTO;
import com.library.dto.CreatePostRequest;
import com.library.dto.UpdatePostRequest;
import com.library.dto.PageResponse;
import com.library.entity.Post;
import com.library.entity.User;
import com.library.exception.BusinessException;
import com.library.common.ResponseCode;
import com.library.repository.PostRepository;
import com.library.repository.UserRepository;
import com.library.service.PostService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public PageResponse<PostDTO> searchPosts(String keyword, Boolean isActive, Pageable pageable) {
        Page<Post> posts = postRepository.searchPosts(keyword, isActive, pageable);
        List<PostDTO> postDTOs = posts.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageResponse<>(
                postDTOs,
                posts.getNumber(),
                posts.getSize(),
                posts.getTotalElements(),
                posts.getTotalPages(),
                posts.isLast()
        );
    }

    @Override
    public PostDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND, "Post not found"));
        return convertToDTO(post);
    }

    @Override
    public PostDTO createPost(CreatePostRequest request) {
        User author = userRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND, "User not found"));

        Post post = modelMapper.map(request, Post.class);
        post.setAuthor(author);
        post.setActive(true);
        post.setLikeCount(0L);

        Post savedPost = postRepository.save(post);
        return convertToDTO(savedPost);
    }

    @Override
    public PostDTO updatePost(Long id, UpdatePostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND, "Post not found"));

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        Post updatedPost = postRepository.save(post);
        return convertToDTO(updatedPost);
    }

    @Override
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND, "Post not found"));
        post.setDeleted(true);
        postRepository.save(post);
    }

    @Override
    public boolean isOwner(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND, "Post not found"));
        return post.getAuthor().getUsername().equals(username);
    }

    @Override
    public List<Map<String, Object>> getTopLikedPosts(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return postRepository.findTopLikedPosts(pageable).stream()
                .map(post -> {
                    Map<String, Object> map = Map.of(
                        "id", post.getId(),
                        "title", post.getTitle(),
                        "author", post.getAuthor().getUsername(),
                        "likeCount", post.getLikeCount()
                    );
                    return map;
                })
                .collect(Collectors.toList());
    }

    private PostDTO convertToDTO(Post post) {
        return PostDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorID(post.getAuthor().getId())
                .likeCount(post.getLikeCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .isActive(post.isActive())
                .isDeleted(post.isDeleted())
                .build();
    }
} 