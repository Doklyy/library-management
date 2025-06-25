package com.library.repository;

import com.library.entity.User;
import com.library.entity.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByIdentityNumber(String identityNumber);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByIdentityNumber(String identityNumber);
    Page<User> findByUsernameContainingOrEmailContaining(String username, String email, Pageable pageable);

    @Query("SELECT u FROM User u WHERE " +
           "(:keyword IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:isActive IS NULL OR u.isActive = :isActive) " +
           "AND (:role IS NULL OR u.role = :role)")
    Page<User> searchUsers(
        @Param("keyword") String keyword,
        @Param("isActive") Boolean isActive,
        @Param("role") UserRole role,
        Pageable pageable
    );
}