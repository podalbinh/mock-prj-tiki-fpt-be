package com.vn.backend.controllers;

import com.vn.backend.dto.request.CreateUserRequest;
import com.vn.backend.dto.request.UpdateUserRequest;
import com.vn.backend.dto.response.ResponseData;
import com.vn.backend.dto.response.UserResponse;
import com.vn.backend.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    // 1. GET /api/users - Lấy danh sách users với phân trang và tìm kiếm
    @GetMapping
    public ResponseData<List<UserResponse>> getAllUsers(
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        logger.info("[IN] GET /api/users - Keyword: {}, Page: {}, Size: {}", 
                   keyword, pageable.getPageNumber(), pageable.getPageSize());
        var response = ResponseData.success(userService.getAllUsers(keyword, pageable));
        logger.info("[OUT] GET /api/users");
        return response;
    }

    // 2. GET /api/users/{id} - Lấy thông tin user theo ID
    @GetMapping("/{id}")
    public ResponseData<UserResponse> getUserById(@PathVariable Long id) {
        logger.info("[IN] GET /api/users/{}", id);
        var response = ResponseData.success(userService.getUserById(id));
        logger.info("[OUT] GET /api/users/{}", id);
        return response;
    }

    // 3. POST /api/users - Tạo user mới (Admin only)
    @PostMapping
    public ResponseData<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        logger.info("[IN] POST /api/users - Email: {}", request.getEmail());
        var response = ResponseData.created(userService.createUser(request));
        logger.info("[OUT] POST /api/users");
        return response;
    }

    // 4. PUT /api/users/{id} - Cập nhật thông tin user
    @PutMapping("/{id}")
    public ResponseData<UserResponse> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        logger.info("[IN] PUT /api/users/{}", id);
        var response = ResponseData.success(userService.updateUser(id, request));
        logger.info("[OUT] PUT /api/users/{}", id);
        return response;
    }

    // 5. DELETE /api/users/{id} - Xóa user (Admin only)
    @DeleteMapping("/{id}")
    public ResponseData<String> deleteUser(@PathVariable Long id) {
        logger.info("[IN] DELETE /api/users/{}", id);
        userService.deleteUser(id);
        var response = ResponseData.success("Xóa user thành công");
        logger.info("[OUT] DELETE /api/users/{}", id);
        return response;
    }
}
