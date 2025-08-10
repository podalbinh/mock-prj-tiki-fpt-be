package com.vn.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductHPResponse {
    private Long id;
    private String title;  // map products.name
    private String author;  
    private Double price;   // map products.price
    private Double originalPrice;
    private Double discount;  
    private Double rating;  // map reviews.rating
    private Integer sold;    // map order_items.quantity
    private String thumbnailUrl;  // map products.thumbnail_url
    private Boolean hasAd;
    private Boolean hasTikiNow;
    private Boolean isTopDeal;
    private Boolean isFreeshipXtra;
    private Boolean isAuthentic;
    
    
    // Getters and Setters
    public void mockData(){
        // Random author names
        String[] authors = {"Nguyễn Nhật Ánh", "Tô Hoài", "Nam Cao", "Vũ Trọng Phụng", "Thạch Lam", "Nguyễn Tuân", "Xuân Diệu", "Hồ Chí Minh"};
        this.author = authors[(int) (Math.random() * authors.length)];
        
                 // Random discount percentage (5% to 50%) - bỏ hết phần thập phân
        this.discount = (double) Math.round(Math.random() * 45 + 5);

        // Calculate original price based on discount percentage
        if (this.price != null) {
            // originalPrice = price / (1 - discount/100) - bỏ hết phần thập phân
            this.originalPrice = (double) Math.round(this.price / (1 - this.discount / 100.0));
        } else {
            // If no price, generate random price first - bỏ hết phần thập phân
            this.price = (double) Math.round((Math.random() * 500000) + 100000);
            this.originalPrice = (double) Math.round(this.price / (1 - this.discount / 100.0));
        }
        
        // Random rating (3.5 to 5.0) if not set - bỏ hết phần thập phân
        if (this.rating == null) {
            this.rating = (double) Math.round(2.5 + (Math.random() * 1.5));
        }
        
        // Random sold quantity (0 to 1000) if not set
        if (this.sold == null) {
            this.sold = (int) (Math.random() * 1000);
        }
        
        // Random boolean flags
        this.hasAd = Math.random() > 0.7; // 30% chance
        this.hasTikiNow = Math.random() > 0.6; // 40% chance
        this.isTopDeal = Math.random() > 0.8; // 20% chance
        this.isFreeshipXtra = Math.random() > 0.5; // 50% chance
        this.isAuthentic = Math.random() > 0.9; // 10% chance (rare)
    }
}