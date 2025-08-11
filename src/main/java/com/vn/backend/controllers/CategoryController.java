package com.vn.backend.controllers;

import com.vn.backend.dto.request.CategoryRequest;
import com.vn.backend.dto.response.CategoryResponse;
import com.vn.backend.dto.response.ResponseData;
import com.vn.backend.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping()
    public ResponseData<List<CategoryResponse>> getAllCategories(Pageable pageable) {
        var response = ResponseData.success(categoryService.getAllCategories(pageable));
        return response;
    }

    @GetMapping("/{id}")
    public ResponseData<CategoryResponse> getCategoryById(@PathVariable Long id) {
        var response = ResponseData.success(categoryService.getCategoryById(id));
        return response;
    }

    @PostMapping()
    public ResponseData<CategoryResponse> createCategory(@RequestBody CategoryRequest categoryRequest) {
        var response = ResponseData.created(categoryService.createCategory(categoryRequest));
        return response;
    }

    @PutMapping("/{id}")
    public ResponseData<CategoryResponse> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest) {
        var response = ResponseData.success(categoryService.updateCategory(id, categoryRequest));
        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseData<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        var response = ResponseData.success("Xóa category thành công");
        return response;
    }
}
