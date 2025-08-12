package com.vn.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeaturedCollectionsResponse {
        private String logo;           // thumbnail sponsor
        private String title;          // Product name
        private String sponsor;        // Sponsor name
        private String ratingText;     // Text representation of rating Example: 5/5
        private List<FCProductResponse> listProduct;  // List of product images
        private Double rating;         // Rating list Product max rating with sponsor

    public void mockDataSponsor(){
        this.logo = "https://res.cloudinary.com/dgvl8woja/image/upload/v1754798989/logo_rtcd5u.jpg";
        // Random selection between two title options
        String[] titles = {"Top sách bán chạy", "Bộ sưu tập sách mới giảm đến"};
        this.title = titles[(int) (Math.random() * titles.length)];
        this.sponsor = "1980 Books Tại Tiki Trading";
        // Random rating between 4.5-5.0
        double randomRating = 4.5 + (Math.random() * 0.5);
        this.ratingText = String.format("%.1f/5", randomRating);
        this.rating = randomRating;
    }

}
