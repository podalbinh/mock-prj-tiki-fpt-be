package com.vn.backend.dto.request;

import com.vn.backend.dto.response.AuthorResponse;
import com.vn.backend.dto.response.CategoryResponse;
import com.vn.backend.dto.response.ProductImageResponse;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductRequest {
    private String name;
    private String description;
    private String shortDescription;
    private BigDecimal listPrice;
    private BigDecimal originalPrice;
    private Integer quantitySold;
    private Float ratingAverage;
    private String publisherVn;
    private String publicationDate;
    private String dimensions;
    private String bookCover;
    private String numberOfPage;
    private Integer stockQuantity;
    private Boolean isActive;

    private List<AuthorResponse> authors;
    private CategoryResponse categories;
    private List<ProductImageResponse> images;

}
