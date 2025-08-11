package com.vn.backend.controllers;

import com.vn.backend.dto.request.CreateProductRequest;
import com.vn.backend.dto.response.ProductResponse;
import com.vn.backend.dto.response.ResponseData;
import com.vn.backend.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseData<ProductResponse> createProduct(@RequestBody CreateProductRequest request) {
        var response = ResponseData.created(productService.createProduct(request));
        return response;
    }

    @GetMapping
    public ResponseData<List<ProductResponse>> getAllProducts(
            @RequestParam(name = "_limit", defaultValue = "") Integer limit,
            @RequestParam(name = "_sort", defaultValue = "id") String sortBy,
            @RequestParam(name = "_order", defaultValue = "asc") String order
    ) {
        Sort sort = order.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        var response = ResponseData.success(productService.getAllProducts(sort, limit));
        return response;
    }

    @GetMapping("/{id}")
    public ResponseData<ProductResponse> getProductById(@PathVariable Long id) {
        var response = ResponseData.success(productService.getProductById(id));
        return response;
    }

    @PutMapping("/{id}")
    public ResponseData<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody CreateProductRequest request) {
        var response = ResponseData.success(productService.updateProduct(id, request));
        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseData<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        var response = ResponseData.success("Xóa category thành công");
        return response;
    }
}
