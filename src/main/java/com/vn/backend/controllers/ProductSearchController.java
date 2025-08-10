package com.vn.backend.controllers;

import com.vn.backend.dto.request.ProductSearchRequest;
import com.vn.backend.dto.response.PageResponse;
import com.vn.backend.dto.response.ProductHPResponse;
import com.vn.backend.dto.response.ResponseData;
import com.vn.backend.services.ProductSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product Search", description = "Product search and filtering APIs")
public class ProductSearchController {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductSearchController.class);
    private final ProductSearchService productSearchService;
    
    @GetMapping("/search")
    @Operation(
        summary = "Search products with pagination and sorting", 
        description = "Search products with various sorting options and filtering by rating. " +
                    "Returns paginated results with product information including ratings, sales data, and mock flags."
    )
    @Parameters({
        @Parameter(
            name = "page", 
            description = "Page number (zero-based indexing). Default: 0", 
            example = "0",
            schema = @io.swagger.v3.oas.annotations.media.Schema(type = "integer", minimum = "0")
        ),
        @Parameter(
            name = "size", 
            description = "Number of products per page. Default: 10, Max: 100", 
            example = "10",
            schema = @io.swagger.v3.oas.annotations.media.Schema(type = "integer", minimum = "1", maximum = "100")
        ),
        @Parameter(
            name = "sortBy", 
            description = "Sorting criteria for products. Available options: " +
                        "<br/>• <b>popular</b> - Sort by best selling (highest sold quantity) " +
                        "<br/>• <b>price_asc</b> - Sort by price from low to high " +
                        "<br/>• <b>price_desc</b> - Sort by price from high to low", 
            example = "popular",
            schema = @io.swagger.v3.oas.annotations.media.Schema(
                type = "string", 
                allowableValues = {"popular", "price_asc", "price_desc"},
                defaultValue = "popular"
            )
        ),
        @Parameter(
            name = "minRating", 
            description = "Minimum rating filter (1.0 to 5.0). Only products with rating >= minRating will be returned. Default: 4.0", 
            example = "4.0",
            schema = @io.swagger.v3.oas.annotations.media.Schema(
                type = "number", 
                minimum = "1.0", 
                maximum = "5.0", 
                defaultValue = "4.0"
            )
        ),

    })
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully retrieved products",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PageResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Bad request - Invalid parameters"
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Internal server error"
        )
    })
    public ResponseData<PageResponse<List<ProductHPResponse>>> searchProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "popular") String sortBy,
            @RequestParam(defaultValue = "4.0") double minRating) {
  
        
        logger.info("[IN] Search products - Page: {}, Size: {}, SortBy: {}, MinRating: {}", 
                   page, size, sortBy, minRating);
        
        // Validate parameters
        if (page < 0) {
            page = 0;
        }
        if (size < 1 || size > 100) {
            size = 10;
        }
        if (minRating < 1.0 || minRating > 5.0) {
            minRating = 4.0;
        }
        
        // Create request object from parameters
        ProductSearchRequest request = new ProductSearchRequest();
        request.setPage(page);
        request.setSize(size);
        request.setSortBy(sortBy);
        request.setMinRating(minRating);
        
        PageResponse<List<ProductHPResponse>> response = productSearchService.searchProducts(request);
        logger.info("[OUT] Search products - Page: {}, Size: {}, SortBy: {}, MinRating: {}", 
        page, size, sortBy, minRating);
        return ResponseData.success(response);
    }
    
    @GetMapping("/category/{categoryId}")
    @Operation(
        summary = "Get products by category ID with pagination", 
        description = "Retrieve products from a specific category with pagination support. " +
                    "Returns products with ratings, sales data, and mock flags."
    )
    @Parameters({
        @Parameter(
            name = "categoryId", 
            description = "ID of the category to filter products", 
            example = "1",
            required = true,
            schema = @io.swagger.v3.oas.annotations.media.Schema(type = "integer", minimum = "1")
        ),
        @Parameter(
            name = "page", 
            description = "Page number (zero-based indexing). Default: 0", 
            example = "0",
            schema = @io.swagger.v3.oas.annotations.media.Schema(type = "integer", minimum = "0")
        ),
        @Parameter(
            name = "size", 
            description = "Number of products per page. Default: 10, Max: 100", 
            example = "10",
            schema = @io.swagger.v3.oas.annotations.media.Schema(type = "integer", minimum = "1", maximum = "100")
        )
    })
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully retrieved products by category",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PageResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Bad request - Invalid parameters"
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Category not found"
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Internal server error"
        )
    })
    public ResponseData<PageResponse<List<ProductHPResponse>>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        logger.info("[IN] Get products by category - CategoryId: {}, Page: {}, Size: {}", categoryId, page, size);
        
        // Validate parameters
        if (page < 0) {
            page = 0;
        }
        if (size < 1 || size > 100) {
            size = 10;
        }
        
        PageResponse<List<ProductHPResponse>> response = productSearchService.getProductsByCategory(categoryId, page, size);
        logger.info("[OUT] Get products by category - CategoryId: {}, Total: {}", categoryId, response.getTotalElements());
         return ResponseData.success(response);
    }
}