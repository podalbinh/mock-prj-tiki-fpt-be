package com.vn.backend.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse {
    private Long id;
    private String name;
    private String thumbnailUrl;
    private Long parentId;
    private String parentName;
    private List<CategoryResponse> subcategories;
}
