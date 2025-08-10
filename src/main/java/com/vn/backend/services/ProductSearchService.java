package com.vn.backend.services;

import com.vn.backend.dto.request.ProductSearchRequest;
import com.vn.backend.dto.response.PageResponse;
import com.vn.backend.dto.response.ProductHPResponse;
import com.vn.backend.entities.Product;
import com.vn.backend.repositories.ProductSearchRepository;
import com.vn.backend.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ProductSearchRepository productSearchRepository;
    private final ProductRepository productRepository;

    public PageResponse<List<ProductHPResponse>> searchProducts(ProductSearchRequest request) {
        Pageable pageable = createPageable(request);
        Page<Object[]> productPage;

        switch (request.getSortBy()) {
            case "popular":
                productPage = productSearchRepository.findProductsByPopularityOnly(
                        request.getMinRating(), pageable);
                break;
            case "price_asc":
                productPage = productSearchRepository.findProductsWithRatingAndSold(
                        request.getMinRating(), pageable);
                break;
            case "price_desc":
                productPage = productSearchRepository.findProductsByPopularity(
                        request.getMinRating(), pageable);
                break;
            default:
                productPage = productSearchRepository.findProductsByPopularityOnly(
                        request.getMinRating(), pageable);
        }

        List<ProductHPResponse> products = productPage.getContent().stream()
                .map(this::mapToProductHPResponse)
                .collect(Collectors.toList());


        return PageResponse.<List<ProductHPResponse>>builder()
                .pageNumber(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalPages(productPage.getTotalPages())
                .totalElements((int) productPage.getTotalElements())
                .last(productPage.isLast())
                .sortBy(request.getSortBy())
                .orderBy(getOrderBy(request.getSortBy()))
                .content(products)
                .build();
    }

    /**
     * Lấy danh sách sản phẩm theo category ID với phân trang
     * @param categoryId ID của danh mục
     * @param page Số trang (bắt đầu từ 0)
     * @param size Kích thước mỗi trang
     * @return PageResponse chứa danh sách ProductHPResponse
     */
    public PageResponse<List<ProductHPResponse>> getProductsByCategory(Long categoryId, int page, int size) {
        // Tạo Pageable object
        Pageable pageable = PageRequest.of(page, size);
        
        // Lấy dữ liệu từ repository
        Page<Object[]> productsPage = productRepository.findProductsByCategoryWithRatingsAndSoldCount(categoryId, pageable);
        
        // Chuyển đổi thành ProductHPResponse
        List<ProductHPResponse> products = productsPage.getContent().stream()
                .map(this::mapToProductHPResponseFromCategory)
                .collect(Collectors.toList());
        
        // Tạo PageResponse
        return PageResponse.<List<ProductHPResponse>>builder()
                .pageNumber(page)
                .pageSize(size)
                .totalPages(productsPage.getTotalPages())
                .totalElements((int) productsPage.getTotalElements())
                .last(productsPage.isLast())
                .content(products)
                .build();
    }

    private Pageable createPageable(ProductSearchRequest request) {
        return PageRequest.of(request.getPage(), request.getSize());
    }

    private String getOrderBy(String sortBy) {
        switch (sortBy) {
            case "popular":
                return "Số lượng bán chạy nhất";
            case "price_asc":
                return "Giá từ thấp đến cao";
            case "price_desc":
                return "Giá từ cao đến thấp";
            default:
                return "Phổ biến";
        }
    }

    // ... existing code ...

    private ProductHPResponse mapToProductHPResponse(Object[] row) {
        ProductHPResponse product = new ProductHPResponse(
                ((Number) row[0]).longValue(), // id
                (String) row[1], // name -> title
                null, // author - sẽ được set bởi mockData()
                ((Number) row[2]).doubleValue(), // price
                null, // originalPrice - sẽ được set bởi mockData()
                null, // discount - sẽ được set bởi mockData()
                ((Number) row[4]).doubleValue(), // rating
                ((Number) row[5]).intValue(), // sold
                (String) row[3], // thumbnailUrl
                null, // hasAd - sẽ được set bởi mockData()
                null, // hasTikiNow - sẽ được set bởi mockData()
                null, // isTopDeal - sẽ được set bởi mockData()
                null, // isFreeshipXtra - sẽ được set bởi mockData()
                null // isAuthentic - sẽ được set bởi mockData()
        );

        // Gọi hàm mockData() để lấy các trường dữ liệu ngẫu nhiên
        product.mockData();

        return product;
    }
    
    /**
     * Chuyển đổi dữ liệu từ Object[] thành ProductHPResponse cho category
     * Query trả về: p, AVG(r.rating), COUNT(r.id), COALESCE(SUM(oi.quantity), 0)
     */
    private ProductHPResponse mapToProductHPResponseFromCategory(Object[] productData) {
        // productData[0] là Product object
        Product product = (Product) productData[0];
        // productData[1] là avgRating
        Double avgRating = productData[1] != null ? ((Number) productData[1]).doubleValue() : null;
        // productData[2] là reviewCount (không cần dùng)
        // productData[3] là soldCount
        Long soldCount = productData[3] != null ? ((Number) productData[3]).longValue() : 0L;
        
        ProductHPResponse response = new ProductHPResponse(
                product.getId(),
                product.getName(), // title
                null, // author - sẽ được set bởi mockData()
                product.getPrice().doubleValue(), // price
                null, // originalPrice - sẽ được set bởi mockData()
                null, // discount - sẽ được set bởi mockData()
                avgRating, // rating
                soldCount.intValue(), // sold
                product.getThumbnailUrl(), // thumbnailUrl
                null, // hasAd - sẽ được set bởi mockData()
                null, // hasTikiNow - sẽ được set bởi mockData()
                null, // isTopDeal - sẽ được set bởi mockData()
                null, // isFreeshipXtra - sẽ được set bởi mockData()
                null // isAuthentic - sẽ được set bởi mockData()
        );

        // Gọi hàm mockData() để lấy các trường dữ liệu ngẫu nhiên
        response.mockData();

        return response;
    }
}
