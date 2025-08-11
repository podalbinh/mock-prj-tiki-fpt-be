package com.vn.backend.services;

import com.vn.backend.dto.request.CategoryRequest;
import com.vn.backend.dto.response.CategoryResponse;
import com.vn.backend.entities.Category;
import com.vn.backend.exceptions.NotFoundException;
import com.vn.backend.repositories.CategoryRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRespository categoryRespository;

    public List<CategoryResponse> getAllCategories(Pageable pageable) {
        Page<Category> categories = categoryRespository.findAll(pageable);
        List<CategoryResponse> categoryResponses = categories.getContent().stream()
                .map(this::convertToCategoryResponse)
                .toList();
        return categoryResponses;
    }

    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRespository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy category với id: " + id));
        return convertToCategoryResponse(category);
    }

    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        Category category = Category.builder()
                .name(categoryRequest.getName())
                .build();
        Category savedCategory = categoryRespository.save(category);
        return convertToCategoryResponse(savedCategory);
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {
        Category category = categoryRespository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy category với id: " + id));
        if(categoryRequest.getName() != null) {
            category.setName(categoryRequest.getName());
        }
        Category updatedCategory = categoryRespository.save(category);
        return convertToCategoryResponse(updatedCategory);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRespository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy category với id: " + id));
        categoryRespository.delete(category);
    }

    private CategoryResponse convertToCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
