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
               COALESCE((SELECT AVG(r.rating) FROM reviews r WHERE r.product_id = p.id), 0) as avg_rating,
               COALESCE((SELECT SUM(oi.quantity) FROM order_items oi WHERE oi.product_id = p.id), 0) as total_sold,
               GROUP_CONCAT(a.name SEPARATOR ', ') as author_names,p.original_price
        FROM products p
        LEFT JOIN authors a ON a.product_id = p.id
        WHERE p.is_active = true
        AND (:categoryId IS NULL OR p.category_id = :categoryId)
        AND COALESCE((SELECT AVG(r.rating) FROM reviews r WHERE r.product_id = p.id), 0) >= COALESCE(:minRating, 0)
        GROUP BY p.id, p.name, p.price, p.thumbnail_url
        ORDER BY p.price ASC
        """, 
        countQuery = """
        SELECT COUNT(p.id)
        FROM products p
        WHERE p.is_active = true
        AND (:categoryId IS NULL OR p.category_id = :categoryId)
        AND COALESCE((SELECT AVG(r.rating) FROM reviews r WHERE r.product_id = p.id), 0) >= COALESCE(:minRating, 0)
        """,
        nativeQuery = true)
    Page<Object[]> findProductsWithRatingAndSold(
        @Param("minRating") Double minRating,
        @Param("categoryId") Long categoryId,
        Pageable pageable
    );
    
    @Query(value = """
        SELECT p.id, p.name, p.price, p.thumbnail_url,
               COALESCE((SELECT AVG(r.rating) FROM reviews r WHERE r.product_id = p.id), 0) as avg_rating,
               COALESCE((SELECT SUM(oi.quantity) FROM order_items oi WHERE oi.product_id = p.id), 0) as total_sold,
               GROUP_CONCAT(a.name SEPARATOR ', ') as author_names,p.original_price
        FROM products p
        LEFT JOIN authors a ON a.product_id = p.id
        WHERE p.is_active = true
        AND (:categoryId IS NULL OR p.category_id = :categoryId)
        AND COALESCE((SELECT AVG(r.rating) FROM reviews r WHERE r.product_id = p.id), 0) >= COALESCE(:minRating, 0)
        GROUP BY p.id, p.name, p.price, p.thumbnail_url
        ORDER BY p.price DESC
        """, 
        countQuery = """
        SELECT COUNT(p.id)
        FROM products p
        WHERE p.is_active = true
        AND (:categoryId IS NULL OR p.category_id = :categoryId)
        AND COALESCE((SELECT AVG(r.rating) FROM reviews r WHERE r.product_id = p.id), 0) >= COALESCE(:minRating, 0)
        """,
        nativeQuery = true)
    Page<Object[]> findProductsByPopularity(
        @Param("minRating") Double minRating,
        @Param("categoryId") Long categoryId,
        Pageable pageable
    );

    @Query(value = """
        SELECT p.id, p.name, p.price, p.thumbnail_url,
               COALESCE((SELECT AVG(r.rating) FROM reviews r WHERE r.product_id = p.id), 0) as avg_rating,
               COALESCE((SELECT SUM(oi.quantity) FROM order_items oi WHERE oi.product_id = p.id), 0) as total_sold,
               GROUP_CONCAT(a.name SEPARATOR ', ') as author_names,p.original_price
        FROM products p
        LEFT JOIN authors a ON a.product_id = p.id
        WHERE p.is_active = true
        AND (:categoryId IS NULL OR p.category_id = :categoryId)
        AND COALESCE((SELECT AVG(r.rating) FROM reviews r WHERE r.product_id = p.id), 0) >= COALESCE(:minRating, 0)
        GROUP BY p.id, p.name, p.price, p.thumbnail_url
        ORDER BY COALESCE((SELECT SUM(oi.quantity) FROM order_items oi WHERE oi.product_id = p.id), 0) DESC
        """, 
        countQuery = """
        SELECT COUNT(p.id)
        FROM products p
        WHERE p.is_active = true
        AND (:categoryId IS NULL OR p.category_id = :categoryId)
        AND COALESCE((SELECT AVG(r.rating) FROM reviews r WHERE r.product_id = p.id), 0) >= COALESCE(:minRating, 0)
        """,
        nativeQuery = true)
    Page<Object[]> findProductsByPopularityOnly(
        @Param("minRating") Double minRating,
        @Param("categoryId") Long categoryId,
        Pageable pageable
    );

    @Query(value = """
    SELECT p.id, p.name, p.price, p.thumbnail_url,
           COALESCE((SELECT AVG(r.rating) FROM reviews r WHERE r.product_id = p.id), 0) as avg_rating,
           COALESCE((SELECT SUM(oi.quantity) FROM order_items oi WHERE oi.product_id = p.id), 0) as total_sold,
           GROUP_CONCAT(a.name SEPARATOR ', ') as author_names,p.original_price
    FROM products p
    LEFT JOIN authors a ON a.product_id = p.id
    WHERE p.is_active = true AND (
        LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(p.short_description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
        GROUP BY p.id, p.name, p.price, p.thumbnail_url
        ORDER BY COALESCE((SELECT SUM(oi.quantity) FROM order_items oi WHERE oi.product_id = p.id), 0) DESC
    """,
    countQuery = """
    SELECT COUNT(p.id) FROM products p
    WHERE p.is_active = true AND (
        LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(p.short_description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
    )
    """,
    nativeQuery = true
    )
    Page<Object[]> searchProductsByKeyword(
            @Param("keyword") String keyword,
            Pageable pageable);
}
