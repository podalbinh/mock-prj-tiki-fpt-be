package com.vn.backend.dto.response;

import java.math.BigDecimal;

public class ItemResponseDTO {
    private Long id;
    private Integer quantity;
    private String name;
    private BigDecimal price;

    public ItemResponseDTO(Long id, Integer quantity, String name, BigDecimal price) {
        this.id = id;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
    }

    // getters và setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
