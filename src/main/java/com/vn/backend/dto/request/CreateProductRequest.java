package com.vn.backend.dto.request;

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
    private String publisherVn;
    private String publicationDate;
    private String dimensions;
    private String bookCover;
    private String numberOfPage;
    private String thumbnail;

    private List<CreateAuthorRequest> authors;
    private Long categoriesId;
    private List<CreateProductImageRequest> images;

}
