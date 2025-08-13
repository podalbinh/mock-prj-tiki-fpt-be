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
    @Query("SELECT p, " +
           "(SELECT AVG(r2.rating) FROM Review r2 WHERE r2.product.id = p.id) as avgRating, " +
           "(SELECT COUNT(r3.id) FROM Review r3 WHERE r3.product.id = p.id) as reviewCount, " +
           "(SELECT COALESCE(SUM(oi2.quantity), 0) FROM OrderItem oi2 " +
           " JOIN Order o2 ON oi2.order.id = o2.id " +
           " WHERE oi2.product.id = p.id AND o2.status != 3) as soldCount " +
           "FROM Product p " +
           "WHERE p.category.id = :categoryId AND p.isActive = true " +
           "ORDER BY p.id DESC")
    Page<Object[]> findProductsByCategoryWithRatingsAndSoldCount(@Param("categoryId") Long categoryId, Pageable pageable);

    List<Product> findByIsActiveTrue(Sort sort);

    @Query("SELECT COALESCE(SUM(oi.quantity), 0) FROM OrderItem oi " +
           "JOIN Order o ON oi.order.id = o.id " +
           "WHERE oi.product.id = :productId AND o.status != 3")
    Long getSoldQuantity(@Param("productId") Long productId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
    Double getRatingAverage(@Param("productId") Long productId);

    // Find all products ordered by quantity sold (descending)
    @Query("SELECT p, " +
           "(SELECT COALESCE(SUM(oi2.quantity), 0) FROM OrderItem oi2 " +
           " JOIN Order o2 ON oi2.order.id = o2.id " +
           " WHERE oi2.product.id = p.id AND o2.status != 3) as soldCount " +
           "FROM Product p " +
           "WHERE p.isActive = true " +
           "ORDER BY (SELECT COALESCE(SUM(oi3.quantity), 0) FROM OrderItem oi3 " +
           "         JOIN Order o3 ON oi3.order.id = o3.id " +
           "         WHERE oi3.product.id = p.id AND o3.status != 3) DESC")
    List<Object[]> findAllProductsOrderByQuantitySoldDesc();

    // Find all products ordered by quantity sold (ascending)
    @Query("SELECT p, " +
           "(SELECT COALESCE(SUM(oi2.quantity), 0) FROM OrderItem oi2 " +
           " JOIN Order o2 ON oi2.order.id = o2.id " +
           " WHERE oi2.product.id = p.id AND o2.status != 3) as soldCount " +
           "FROM Product p " +
           "WHERE p.isActive = true " +
           "ORDER BY (SELECT COALESCE(SUM(oi3.quantity), 0) FROM OrderItem oi3 " +
           "         JOIN Order o3 ON oi3.order.id = o3.id " +
           "         WHERE oi3.product.id = p.id AND o3.status != 3) ASC")
    List<Object[]> findAllProductsOrderByQuantitySoldAsc();

    // Find all products ordered by rating (descending)
    @Query("SELECT p, AVG(r.rating) as avgRating " +
           "FROM Product p " +
           "LEFT JOIN Review r ON p.id = r.product.id " +
           "WHERE p.isActive = true " +
           "GROUP BY p.id " +
           "ORDER BY avgRating DESC NULLS LAST")
    List<Object[]> findAllProductsOrderByRatingDesc();

    // Find all products ordered by rating (ascending)
    @Query("SELECT p, AVG(r.rating) as avgRating " +
           "FROM Product p " +
           "LEFT JOIN Review r ON p.id = r.product.id " +
           "WHERE p.isActive = true " +
           "GROUP BY p.id " +
           "ORDER BY avgRating ASC NULLS LAST")
    List<Object[]> findAllProductsOrderByRatingAsc();


    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p WHERE p.id = :productId AND p.stockQuantity >= :quantity")
    Boolean validateCart(@Param("productId") Long productId, @Param("quantity") Integer quantity);
}
