package com.vn.backend.services;

import com.vn.backend.dto.response.FCProductResponse;
import com.vn.backend.dto.response.FeaturedCollectionsResponse;
import com.vn.backend.entities.Product;
import com.vn.backend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        
        // Map products to FCProductResponse
        List<FCProductResponse> allProductResponses = productsWithRatings.stream()
                .limit(15) // Get more products to create multiple collections
                .map(this::mapToFCProductResponseWithRating)
                .toList();
        
        // Create multiple FeaturedCollectionsResponse, each with 3 products
        List<FeaturedCollectionsResponse> featuredCollections = new ArrayList<>();
        
        for (int i = 0; i < allProductResponses.size(); i += 3) {
            // Get 3 products for this collection
            List<FCProductResponse> collectionProducts = allProductResponses.stream()
                    .skip(i)
                    .limit(3)
                    .collect(Collectors.toList());
            
            // Create new FeaturedCollectionsResponse
            FeaturedCollectionsResponse response = new FeaturedCollectionsResponse();
            response.mockDataSponsor(); // This will set random title and sponsor info
            
            // Calculate rating for this specific collection
            double collectionRating = calculateCollectionRating(collectionProducts);
            response.setRating(collectionRating);
            response.setRatingText(String.format("%.1f/5", collectionRating));
            
            response.setListProduct(collectionProducts);
            featuredCollections.add(response);
        }
        
        return featuredCollections;
    }
    
    private FCProductResponse mapToFCProductResponseWithRating(Object[] productData) {
        Product product = (Product) productData[0];

        return FCProductResponse.builder()
                .id(product.getId())
                .url(product.getThumbnailUrl())
                .discountPercent(generateRandomDiscount()) // Mock discount for now
                .build();
    }


    
    private double calculateCollectionRating(List<FCProductResponse> products) {
        if (products.isEmpty()) {
            return 0.0;
        }
        // For now, generate a random rating between 4.5-5.0 for each collection
        // In the future, this could be calculated from actual product ratings
        return 4.5 + (Math.random() * 0.5);
    }
    
    private Integer generateRandomDiscount() {
        // Generate random discount between 5% and 50%
        return (int) (Math.random() * 46) + 5;
    }
}
