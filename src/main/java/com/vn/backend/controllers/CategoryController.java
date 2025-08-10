package com.vn.backend.controllers;

import com.vn.backend.dto.response.CategoryResponse;
import com.vn.backend.dto.response.ResponseData;
import com.vn.backend.services.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {
    
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    
    @Autowired
    private CategoryService categoryService;
    
    /**
     * GET /api/categories
     * Get root categories with their subcategories for sidebar display
     * @return List of categories with hierarchical structure
     */
    @GetMapping
    public ResponseData<List<CategoryResponse>> getCategoriesWithSubcategories() {
        logger.info("[IN] GET /api/categories");
        List<CategoryResponse> categories = categoryService.getCategoriesWithSubcategories();
        logger.info("[OUT] GET /api/categories - Success, found {} root categories with subcategories", categories.size());
        return ResponseData.success(categories);
    }
    
    /**
     * GET /api/categories/root-with-thumbnail
     * Get root categories that have thumbnail
     * @return List of root categories with thumbnail
     */
    @GetMapping("/root-with-thumbnail")
    public ResponseData<List<CategoryResponse>> getRootCategoriesWithThumbnail() {
        logger.info("[IN] GET /api/categories/root-with-thumbnail");
        List<CategoryResponse> categories = categoryService.getRootCategoriesWithThumbnail();
        logger.info("[OUT] GET /api/categories/root-with-thumbnail - Success, found {} root categories with thumbnail", categories.size());
        return ResponseData.success(categories);
    }

}
