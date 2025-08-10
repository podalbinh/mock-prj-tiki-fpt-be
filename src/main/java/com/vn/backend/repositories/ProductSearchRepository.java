package com.vn.backend.repositories;

import com.vn.backend.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSearchRepository extends JpaRepository<Product, Long> {
    
    @Query(value = """
        SELECT p.id, p.name, p.price, p.thumbnail_url,
               COALESCE(AVG(r.rating), 0) as avg_rating,
               COALESCE(SUM(oi.quantity), 0) as total_sold
        FROM products p
        LEFT JOIN reviews r ON p.id = r.product_id
        LEFT JOIN order_items oi ON p.id = oi.product_id
        WHERE p.is_active = true
        AND (:categoryId IS NULL OR p.category_id = :categoryId)
        GROUP BY p.id, p.name, p.price, p.thumbnail_url
        HAVING COALESCE(AVG(r.rating), 0) >= COALESCE(:minRating, 0)
        ORDER BY p.price ASC
        """, 
        countQuery = """
        SELECT COUNT(DISTINCT p.id)
        FROM products p
        LEFT JOIN reviews r ON p.id = r.product_id
        WHERE p.is_active = true
        AND (:categoryId IS NULL OR p.category_id = :categoryId)
        GROUP BY p.id
        HAVING COALESCE(AVG(r.rating), 0) >= COALESCE(:minRating, 0)
        """,
        nativeQuery = true)
    Page<Object[]> findProductsWithRatingAndSold(
        @Param("minRating") Double minRating,
        @Param("categoryId") Long categoryId,
        Pageable pageable
    );
    
    @Query(value = """
        SELECT p.id, p.name, p.price, p.thumbnail_url,
               COALESCE(AVG(r.rating), 0) as avg_rating,
               COALESCE(SUM(oi.quantity), 0) as total_sold
        FROM products p
        LEFT JOIN reviews r ON p.id = r.product_id
        LEFT JOIN order_items oi ON p.id = oi.product_id
        WHERE p.is_active = true
        AND (:categoryId IS NULL OR p.category_id = :categoryId)
        GROUP BY p.id, p.name, p.price, p.thumbnail_url
        HAVING COALESCE(AVG(r.rating), 0) >= COALESCE(:minRating, 0)
        ORDER BY p.price DESC
        """, 
        countQuery = """
        SELECT COUNT(DISTINCT p.id)
        FROM products p
        LEFT JOIN reviews r ON p.id = r.product_id
        WHERE p.is_active = true
        AND (:categoryId IS NULL OR p.category_id = :categoryId)
        GROUP BY p.id
        HAVING COALESCE(AVG(r.rating), 0) >= COALESCE(:minRating, 0)
        """,
        nativeQuery = true)
    Page<Object[]> findProductsByPopularity(
        @Param("minRating") Double minRating,
        @Param("categoryId") Long categoryId,
        Pageable pageable
    );

    @Query(value = """
        SELECT p.id, p.name, p.price, p.thumbnail_url,
               COALESCE(AVG(r.rating), 0) as avg_rating,
               COALESCE(SUM(oi.quantity), 0) as total_sold
        FROM products p
        LEFT JOIN reviews r ON p.id = r.product_id
        LEFT JOIN order_items oi ON p.id = oi.product_id
        WHERE p.is_active = true
        AND (:categoryId IS NULL OR p.category_id = :categoryId)
        GROUP BY p.id, p.name, p.price, p.thumbnail_url
        HAVING COALESCE(AVG(r.rating), 0) >= COALESCE(:minRating, 0)
        ORDER BY COALESCE(SUM(oi.quantity), 0) DESC
        """, 
        countQuery = """
        SELECT COUNT(DISTINCT p.id)
        FROM products p
        LEFT JOIN reviews r ON p.id = r.product_id
        WHERE p.is_active = true
        AND (:categoryId IS NULL OR p.category_id = :categoryId)
        GROUP BY p.id
        HAVING COALESCE(AVG(r.rating), 0) >= COALESCE(:minRating, 0)
        """,
        nativeQuery = true)
    Page<Object[]> findProductsByPopularityOnly(
        @Param("minRating") Double minRating,
        @Param("categoryId") Long categoryId,
        Pageable pageable
    );

}
