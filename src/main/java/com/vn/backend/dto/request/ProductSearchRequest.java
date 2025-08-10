package com.vn.backend.dto.request;

import lombok.Data;

@Data
public class ProductSearchRequest {
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "popular"; // popular, price_asc, price_desc
    private Double minRating; // Lọc theo rating chỉ khi được gửi lên
}
