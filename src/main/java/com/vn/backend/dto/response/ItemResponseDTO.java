package com.vn.backend.dto.response;

import java.math.BigDecimal;

public class ItemResponseDTO {
    private Long id;
    private Integer quantity;
    private String name;
    private BigDecimal price;
    private String thumbnail;

    public ItemResponseDTO(Long id, Integer quantity, String name, BigDecimal price, String thumbnail) {
        this.id = id;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
        this.thumbnail = thumbnail;
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

    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }
}
