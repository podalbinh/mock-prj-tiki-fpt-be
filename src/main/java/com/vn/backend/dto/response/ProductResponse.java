package com.vn.backend.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
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
    private String thumbnail;

    private List<AuthorResponse> authors;
    private Long categoriesId;
    private List<ProductImageResponse> images;
}
