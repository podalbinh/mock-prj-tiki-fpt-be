package com.vn.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FCProductResponse {
    private Long id;  // id product
    private String url;  // thumbnail Product
    private Integer discountPercent;   // promotion discountPercent
}