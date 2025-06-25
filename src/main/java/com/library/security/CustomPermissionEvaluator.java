package com.library.security;

import com.library.entity.Post;
import com.library.entity.Comment;
import com.library.entity.User;
import com.library.repository.PostRepository;
import com.library.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || targetDomainObject == null || !(permission instanceof String)) {
            return false;
        }

        String targetType = targetDomainObject.getClass().getSimpleName().toUpperCase();
        return hasPrivilege(authentication, targetType, permission.toString().toUpperCase());
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if (authentication == null || targetType == null || !(permission instanceof String)) {
            return false;
        }

        User user = (User) authentication.getPrincipal();
        
        // Kiểm tra quyền sở hữu cho bài viết
        if ("POST".equals(targetType)) {
            Post post = postRepository.findById((Long) targetId).orElse(null);
            if (post != null && post.getAuthor().getId().equals(user.getId())) {
                return true;
            }
        }
        
        // Kiểm tra quyền sở hữu cho comment
        if ("COMMENT".equals(targetType)) {
            Comment comment = commentRepository.findById((Long) targetId).orElse(null);
            if (comment != null && comment.getUser().getId().equals(user.getId())) {
                return true;
            }
        }

        return hasPrivilege(authentication, targetType, permission.toString().toUpperCase());
    }

    private boolean hasPrivilege(Authentication authentication, String targetType, String permission) {
        User user = (User) authentication.getPrincipal();
        String role = user.getRole().name();
        
        // Kiểm tra quyền dựa trên role.properties
        String permissionKey = role + "." + targetType.toLowerCase() + "." + permission.toLowerCase();
        return user.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(permissionKey));
    }
} 