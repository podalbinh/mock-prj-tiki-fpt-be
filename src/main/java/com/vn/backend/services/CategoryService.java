package com.vn.backend.services;

import com.vn.backend.dto.request.CategoryRequest;
import com.vn.backend.dto.response.CategoryResponse;
import com.vn.backend.dto.response.CategoryResponse1;
import com.vn.backend.entities.Category;
import com.vn.backend.exceptions.NotFoundException;
import com.vn.backend.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Get root categories with their subcategories for sidebar display
     * @return List of CategoryResponse with hierarchical structure
     */
    public List<CategoryResponse> getCategoriesWithSubcategories() {
        // Get root categories first
        List<Category> rootCategories = categoryRepository.findByParentIsNull();

        return rootCategories.stream()
                .map(this::mapToCategoryResponseWithSubcategories)
                .collect(Collectors.toList());
    }

    /**
     * Get root categories that have thumbnail
     * @return List of CategoryResponse for root categories with thumbnail
     */
    public List<CategoryResponse> getRootCategoriesWithThumbnail() {
        List<Category> rootCategoriesWithThumbnail = categoryRepository.findRootCategoriesWithThumbnail();
        return rootCategoriesWithThumbnail.stream()
                .map(this::mapToCategoryResponse)
                .collect(Collectors.toList());
    }


    /**
     * Map Category entity to CategoryResponse DTO with subcategories
     * @param category Category entity
     * @return CategoryResponse DTO with subcategories
     */
    private CategoryResponse mapToCategoryResponseWithSubcategories(Category category) {
        // Get subcategories for this category
        List<Category> subcategories = categoryRepository.findByParent(category);
        List<CategoryResponse> subcategoryResponses = subcategories.stream()
                .map(this::mapToCategoryResponse)
                .collect(Collectors.toList());

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .thumbnailUrl(category.getThumbnailUrl())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .parentName(category.getParent() != null ? category.getParent().getName() : null)
                .subcategories(subcategoryResponses)
                .build();
    }

    /**
     * Map Category entity to CategoryResponse DTO
     * @param category Category entity
     * @return CategoryResponse DTO
     */
    private CategoryResponse mapToCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .thumbnailUrl(category.getThumbnailUrl())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .parentName(category.getParent() != null ? category.getParent().getName() : null)
                .build();
    }

    public List<CategoryResponse1> getAllCategories(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.getContent().stream()
                .map(this::convertToCategoryResponse)
                .toList();
    }

    public CategoryResponse1 getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy category với id: " + id));
        return convertToCategoryResponse(category);
    }

    public CategoryResponse1 createCategory(CategoryRequest categoryRequest) {
        Category category = Category.builder()
                .name(categoryRequest.getName())
                .build();
        Category savedCategory = categoryRepository.save(category);
        return convertToCategoryResponse(savedCategory);
    }

    public CategoryResponse1 updateCategory(Long id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy category với id: " + id));
        if(categoryRequest.getName() != null) {
            category.setName(categoryRequest.getName());
        }
        Category updatedCategory = categoryRepository.save(category);
        return convertToCategoryResponse(updatedCategory);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy category với id: " + id));
        categoryRepository.delete(category);
    }

    private CategoryResponse1 convertToCategoryResponse(Category category) {
        return CategoryResponse1.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
