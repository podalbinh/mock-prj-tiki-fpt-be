package com.vn.backend.dto.request;

import com.vn.backend.dto.response.AuthorResponse;
import com.vn.backend.dto.response.CategoryResponse;
import com.vn.backend.dto.response.ProductImageResponse;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductImageRequest {
    private String baseUrl;
    private Boolean isGallery;
    private String label;
    private String largeUrl;
    private String mediumUrl;
    private String smallUrl;
    private String thumbnailUrl;
    private String altText;
    private Long productId; // id của product liên kết
}
