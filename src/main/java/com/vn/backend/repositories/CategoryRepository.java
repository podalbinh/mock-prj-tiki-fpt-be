package com.vn.backend.repositories;

import com.vn.backend.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    // Find all categories
    List<Category> findAll();
    
    // Find categories by parent (for hierarchical structure)
    List<Category> findByParent(Category parent);
    
    // Find root categories (no parent)
    List<Category> findByParentIsNull();
    
    /**
     * Find root categories (no parent) that have thumbnail
     * @return List of root categories with thumbnail
     */
    @Query("SELECT c FROM Category c WHERE c.parent IS NULL AND c.thumbnailUrl IS NOT NULL AND c.thumbnailUrl != ''")
    List<Category> findRootCategoriesWithThumbnail();

    // Tìm kiếm categories theo tên (có phân trang)
    @Query("SELECT c FROM Category c WHERE c.name LIKE %:keyword%")
    Page<Category> findByNameContaining(@Param("keyword") String keyword, Pageable pageable);

}
