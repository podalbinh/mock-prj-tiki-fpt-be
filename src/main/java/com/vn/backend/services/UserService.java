package com.vn.backend.services;

import com.vn.backend.dto.request.CreateUserRequest;
import com.vn.backend.dto.request.UpdateUserRequest;
import com.vn.backend.dto.response.PagedResponse;
import com.vn.backend.dto.response.UserResponse;
import com.vn.backend.entities.User;
import com.vn.backend.exceptions.InvalidDataException;
import com.vn.backend.exceptions.NotFoundException;
import com.vn.backend.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    // 1. Lấy danh sách users với phân trang và tìm kiếm
    public PagedResponse<UserResponse> getAllUsers(String keyword, Pageable pageable) {
        return getAllUsers(keyword, pageable, false); // Mặc định hiển thị tất cả users
    }
    
    // 1a. Lấy danh sách users với tùy chọn chỉ hiển thị user active
    public PagedResponse<UserResponse> getAllUsers(String keyword, Pageable pageable, boolean activeOnly) {
        logger.info("[IN] Get all users - Keyword: {}, Page: {}, Size: {}, ActiveOnly: {}", 
                   keyword, pageable.getPageNumber(), pageable.getPageSize(), activeOnly);
        
        // Tạo sort mặc định nếu không có sort parameter
        Pageable pageableWithDefaultSort = pageable;
        if (pageable.getSort().isUnsorted()) {
            Sort defaultSort = Sort.by(Sort.Direction.DESC, "createdAt");
            pageableWithDefaultSort = PageRequest.of(
                pageable.getPageNumber(), 
                pageable.getPageSize(), 
                defaultSort
            );
        }
        
        Page<User> users;
        
        if (activeOnly) {
            // Chỉ lấy user active
            if (keyword != null && !keyword.trim().isEmpty()) {
                users = userRepository.findActiveUsersByKeyword(keyword.trim(), pageableWithDefaultSort);
            } else {
                users = userRepository.findAllActiveUsers(pageableWithDefaultSort);
            }
        } else {
            // Lấy tất cả users (bao gồm cả inactive)
            if (keyword != null && !keyword.trim().isEmpty()) {
                users = userRepository.findByKeyword(keyword.trim(), pageableWithDefaultSort);
            } else {
                users = userRepository.findAll(pageableWithDefaultSort);
            }
        }
        
        List<UserResponse> userResponses = users.getContent().stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
        
        PagedResponse<UserResponse> response = PagedResponse.<UserResponse>builder()
                .data(userResponses)
                .totalElements(users.getTotalElements())
                .totalPages(users.getTotalPages())
                .currentPage(users.getNumber())
                .pageSize(users.getSize())
                .hasNext(users.hasNext())
                .hasPrevious(users.hasPrevious())
                .build();
        
        logger.info("[OUT] Get all users - Total: {}, Page: {}/{}, ActiveOnly: {}", 
                   users.getTotalElements(), users.getNumber() + 1, users.getTotalPages(), activeOnly);
        return response;
    }    // 2. Lấy user theo ID
    public UserResponse getUserById(Long id) {
        logger.info("[IN] Get user by ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy user với ID: " + id));
        UserResponse response = convertToUserResponse(user);
        logger.info("[OUT] Get user by ID: {}", id);
        return response;
    }

    // 3. Tạo user mới
    public UserResponse createUser(CreateUserRequest request) {
        logger.info("[IN] Create user with email: {}", request.getEmail());
        
        // Kiểm tra email đã tồn tại
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new InvalidDataException("Email đã tồn tại: " + request.getEmail());
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .role(request.getRole() != null ? request.getRole() : com.vn.backend.utils.enums.Role.USER)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .avatarUrl(request.getAvatarUrl())
                .build();

        User savedUser = userRepository.save(user);
        UserResponse response = convertToUserResponse(savedUser);
        logger.info("[OUT] Create user with ID: {}", savedUser.getId());
        return response;
    }

    // 4. Cập nhật user
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        logger.info("[IN] Update user with ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy user với ID: " + id));

        // Cập nhật các field nếu có
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
        if (request.getIsActive() != null) {
            user.setIsActive(request.getIsActive());
        }

        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }

        User updatedUser = userRepository.save(user);
        UserResponse response = convertToUserResponse(updatedUser);
        logger.info("[OUT] Update user with ID: {}", id);
        return response;
    }

    // 5. Xóa user (soft delete để tránh foreign key constraint)
    public void deleteUser(Long id) {
        logger.info("[IN] Delete user with ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy user với ID: " + id));
        
        try {
            // Thử xóa trực tiếp trước
            userRepository.delete(user);
            logger.info("[OUT] Delete user with ID: {} - Hard delete successful", id);
        } catch (Exception e) {
            // Nếu có foreign key constraint, thực hiện soft delete
            logger.warn("[WARN] Cannot hard delete user with ID: {} due to foreign key constraints. Performing soft delete.", id);
            user.setIsActive(false);
            user.setEmail("deleted_" + user.getId() + "_" + user.getEmail()); // Để tránh conflict email khi tạo user mới
            userRepository.save(user);
            logger.info("[OUT] Delete user with ID: {} - Soft delete successful", id);
        }
    }

    // Helper method để convert User entity thành UserResponse
    private UserResponse convertToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .isActive(user.getIsActive())
                .role(user.getRole())
                .avatarUrl(user.getAvatarUrl())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
