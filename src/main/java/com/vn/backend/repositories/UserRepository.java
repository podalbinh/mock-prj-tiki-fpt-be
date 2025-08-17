package com.vn.backend.repositories;

import com.vn.backend.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    
    // Tìm kiếm users theo keyword trong email, fullName, phone
    @Query("SELECT u FROM User u WHERE " +
           "u.email LIKE %:keyword% OR " +
           "u.fullName LIKE %:keyword% OR " +
           "u.phone LIKE %:keyword%")
    Page<User> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    // Tìm kiếm users active theo keyword
    @Query("SELECT u FROM User u WHERE u.isActive = true AND (" +
           "u.email LIKE %:keyword% OR " +
           "u.fullName LIKE %:keyword% OR " +
           "u.phone LIKE %:keyword%)")
    Page<User> findActiveUsersByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    // Lấy tất cả users active
    @Query("SELECT u FROM User u WHERE u.isActive = true")
    Page<User> findAllActiveUsers(Pageable pageable);
    
    // Dashboard queries
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :startDate AND u.createdAt <= :endDate")
    Long countUsersByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
