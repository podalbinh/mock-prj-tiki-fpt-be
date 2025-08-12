package com.vn.backend.repositories;

import com.vn.backend.entities.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    void deleteByProduct_Id(Long productId);
}
