package com.vn.backend.services;

import com.vn.backend.dto.response.FCProductResponse;
import com.vn.backend.dto.response.FeaturedCollectionsResponse;
import com.vn.backend.entities.Product;
import com.vn.backend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class FeaturedCollectionsService {
    
    @Autowired
    private ProductRepository productRepository;
    
    public List<FeaturedCollectionsResponse> getFeaturedCollections() {
        // Query active products with ratings from database
        List<Object[]> productsWithRatings = productRepository.findActiveProductsWithRatings();
        
        // Map to internal holder that keeps product response and its average rating
        List<ProductWithRating> products = productsWithRatings.stream()
                .limit(15)
                .map(this::mapToProductWithRating)
                .toList();
        
        // Create multiple FeaturedCollectionsResponse, each with 3 products
        List<FeaturedCollectionsResponse> featuredCollections = new ArrayList<>();
        
        for (int i = 0; i < products.size(); i += 3) {
            // Get 3 products for this collection
            List<ProductWithRating> collection = products.stream()
                    .skip(i)
                    .limit(3)
                    .collect(Collectors.toList());

            List<FCProductResponse> collectionProducts = collection.stream()
                    .map(ProductWithRating::productResponse)
                    .collect(Collectors.toList());
            
            // Create new FeaturedCollectionsResponse
            FeaturedCollectionsResponse response = new FeaturedCollectionsResponse();
            response.mockDataSponsor(); // This will set random title and sponsor info
            
            // Calculate rating for this specific collection
            double collectionRating = calculateCollectionRatingFromAverages(collection);
            response.setRating(collectionRating);
            response.setRatingText(String.format("%.1f/5", collectionRating));
            
            response.setListProduct(collectionProducts);
            featuredCollections.add(response);
        }
        
        return featuredCollections;
    }
    
    private ProductWithRating mapToProductWithRating(Object[] productData) {
        Product product = (Product) productData[0];
        Double avgRating = productData.length > 1 && productData[1] != null
                ? ((Number) productData[1]).doubleValue()
                : null;

        FCProductResponse response = new FCProductResponse();
        response.setId(product.getId());
        response.setUrl(product.getThumbnailUrl());
        response.setDiscountPercent(calculateDiscountPercent(product));

        return new ProductWithRating(response, avgRating);
    }


    
    private double calculateCollectionRatingFromAverages(List<ProductWithRating> products) {
        if (products == null || products.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        int count = 0;
        for (ProductWithRating item : products) {
            if (item.averageRating() != null) {
                sum += item.averageRating();
                count += 1;
            }
        }
        if (count == 0) {
            return 0.0;
        }
        double avg = sum / count;
        // Clamp rating between 0 and 5
        if (avg < 0) avg = 0.0;
        if (avg > 5) avg = 5.0;
        return Math.round(avg * 10.0) / 10.0;
    }

    private Integer calculateDiscountPercent(Product product) {
        if (product == null) return 0;
        BigDecimal originalPrice = product.getOriginalPrice();
        BigDecimal currentPrice = product.getPrice();
        if (originalPrice == null || currentPrice == null) return 0;
        if (originalPrice.compareTo(BigDecimal.ZERO) <= 0) return 0;
        if (currentPrice.compareTo(originalPrice) >= 0) return 0;

        BigDecimal discount = originalPrice.subtract(currentPrice);
        BigDecimal percent = discount
                .divide(originalPrice, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
        return percent.setScale(0, RoundingMode.HALF_UP).intValue();
    }

    private static class ProductWithRating {
        private final FCProductResponse productResponse;
        private final Double averageRating;

        public ProductWithRating(FCProductResponse productResponse, Double averageRating) {
            this.productResponse = productResponse;
            this.averageRating = averageRating;
        }

        public FCProductResponse productResponse() {
            return productResponse;
        }

        public Double averageRating() {
            return averageRating;
        }
    }
}
