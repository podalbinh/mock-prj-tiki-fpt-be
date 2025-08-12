package com.vn.backend.repositories;

import com.vn.backend.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Find active products
    List<Product> findByIsActiveTrue();

    // Find products for featured collections
    @Query("SELECT p FROM Product p WHERE p.isActive = true ORDER BY p.id DESC")
    List<Product> findActiveProductsForFeaturedCollections();

    // Find products with limit for featured collections
    @Query("SELECT p FROM Product p WHERE p.isActive = true ORDER BY p.id DESC")
    List<Product> findTopActiveProducts(@Param("limit") int limit);

    // Find products with average rating for featured collections
    @Query("SELECT p, AVG(r.rating) as avgRating, COUNT(r.id) as reviewCount " +
           "FROM Product p " +
           "LEFT JOIN Review r ON p.id = r.product.id " +
           "WHERE p.isActive = true " +
           "GROUP BY p.id " +
           "ORDER BY avgRating DESC NULLS LAST, p.id DESC")
    List<Object[]> findActiveProductsWithRatings();

    // Find products by category ID with pagination
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.isActive = true")
    Page<Product> findByCategoryIdAndIsActiveTrue(@Param("categoryId") Long categoryId, Pageable pageable);

    // Find products by category ID with ratings and sold count for homepage
    @Query("SELECT p, AVG(r.rating) as avgRating, COUNT(r.id) as reviewCount, " +
           "COALESCE(SUM(oi.quantity), 0) as soldCount " +
           "FROM Product p " +
           "LEFT JOIN Review r ON p.id = r.product.id " +
           "LEFT JOIN OrderItem oi ON p.id = oi.product.id " +
           "WHERE p.category.id = :categoryId AND p.isActive = true " +
           "GROUP BY p.id " +
           "ORDER BY p.id DESC")
    Page<Object[]> findProductsByCategoryWithRatingsAndSoldCount(@Param("categoryId") Long categoryId, Pageable pageable);

    List<Product> findByIsActiveTrue(Sort sort);

    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.product.id = :productId")
    Long getSoldQuantity(@Param("productId") Long productId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
    Double getRatingAverage(@Param("productId") Long productId);
}
