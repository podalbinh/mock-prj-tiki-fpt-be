package com.vn.backend.dto.request;

import com.vn.backend.utils.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private String fullName;
    private String phone;
    private Role role;
    private String avatarUrl;
    private Boolean isActive;
}
