package com.vn.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCategoryRequest {
    
    @NotBlank(message = "Tên category không được để trống")
    @Size(max = 100, message = "Tên category không được vượt quá 100 ký tự")
    private String name;
    
    private Long parentId; // ID của category cha (null nếu là category gốc)
    
    private String thumbnailUrl;
}
