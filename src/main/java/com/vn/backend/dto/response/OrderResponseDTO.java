package com.vn.backend.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class OrderResponseDTO {
    private Long id;
    private String customerName;
    private List<ItemResponseDTO> products;
    private BigDecimal totalPrice;
    private String status;

    public OrderResponseDTO(Long id, String customerName, List<ItemResponseDTO> products, BigDecimal totalPrice, String status) {
        this.id = id;
        this.customerName = customerName;
        this.products = products;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    // getters và setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public List<ItemResponseDTO> getProducts() { return products; }
    public void setProducts(List<ItemResponseDTO> products) { this.products = products; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
