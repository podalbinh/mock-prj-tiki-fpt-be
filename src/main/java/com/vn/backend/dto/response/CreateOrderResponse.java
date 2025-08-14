package com.vn.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderResponse {
    public Long orderId;
    public BigDecimal totalAmount;
    public List<ProductOrderResponse> products;
    public static class ProductOrderResponse{
        public Long productId;
        public String productName;
        public String thumbnailUrl;
        
        public ProductOrderResponse(Long productId, String productName, String thumbnailUrl){
            this.productId = productId;
            this.productName = productName;
            this.thumbnailUrl = thumbnailUrl;
        }
    }
}
