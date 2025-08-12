package com.vn.backend.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse1 {
    Long id;
    String name;
    String thumbnailUrl;
    
    // Thông tin parent category
    Long parentId;
    String parentName;
    CategoryResponse1 parent; // Đối tượng parent đầy đủ
}
